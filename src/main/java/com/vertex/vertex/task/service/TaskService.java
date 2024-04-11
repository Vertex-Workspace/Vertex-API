package com.vertex.vertex.task.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.notification.entity.model.LogRecord;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.notification.repository.LogRepository;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.*;
import com.vertex.vertex.task.relations.review.repository.ReviewRepository;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.TaskDoesNotExistException;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskResponsablesRepository taskResponsablesRepository;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final PropertyService propertyService;
    private final UserTeamService userTeamService;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final FileService fileService;
    private final NotificationService notificationService;
    private final LogRepository logRepository;

    public Task save(TaskCreateDTO taskCreateDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);
        Project project;
        List<Value> values = new ArrayList<>();
        try {
            project = projectService.findById(taskCreateDTO.getProject().getId());
        } catch (Exception e) {
            throw new RuntimeException("There isn't a project with this id is not linked with the current team!");
        }
        //When the task is created, every property is associated with a null value, unless it has a default value
        for (Property property : project.getProperties()) {
            Value currentValue = property.getKind().getValue();
            currentValue.setProperty(property);
            currentValue.setTask(task);
            values.add(currentValue);

            if (property.getKind() == PropertyKind.STATUS) {
                //Get the first element, how the three are fixed, it always will be TO DO "Não Iniciado"
                currentValue.setValue(property.getPropertyLists().get(0));
            }
            if (property.getKind() == PropertyKind.DATE) {
                ((ValueDate) currentValue).setValue();
            }
            if (property.getKind() == PropertyKind.TEXT) {
                currentValue.setValue(property.getDefaultValue());
            }
        }
        task.setValues(values);
        //Add the taskResponsables on task list of taskResponsables
        task.setCreator(userTeamService.findUserTeamByComposeId(project.getTeam().getId(), taskCreateDTO.getCreator().getId()));
        for (UserTeam userTeam : project.getCollaborators()) {
            TaskResponsable newTaskResponsable = new TaskResponsable(userTeam, task);
            if (task.getTaskResponsables() == null) {
                ArrayList<TaskResponsable> taskResponsibleList = new ArrayList<>();
                taskResponsibleList.add(newTaskResponsable);
                task.setTaskResponsables(taskResponsibleList);
            } else {
                task.getTaskResponsables().add(newTaskResponsable);
            }
        }

        if(project.getGroups() != null){
            List<Group> groups = new ArrayList<>(project.getGroups());
            task.setGroups(groups);
        }

        //Set if the task is revisable or no...
        task.setTaskDependency(null);
        task.setRevisable(project.getProjectReviewENUM().equals(ProjectReviewENUM.MANDATORY));


        Task finalTask = taskRepository.save(task);

        //Notifications
        for (TaskResponsable taskResponsable : finalTask.getTaskResponsables()) {
            if (taskResponsable.getUserTeam().getUser().getResponsibleInProjectOrTask()
                    && !taskResponsable.getUserTeam().equals(task.getCreator())) {

                notificationService.save(new Notification(
                        project,
                        "Você foi adicionado como responsável da tarefa " + task.getName(),
                        "projeto/" + project.getId() + "/tarefas?taskID=" + finalTask.getId(),
                        taskResponsable.getUserTeam().getUser()
                ));
            }
        }
        return finalTask;
    }


    public Task edit(TaskEditDTO taskEditDTO) {
        try {
            Task task = findById(taskEditDTO.getId());

            String modifiedAttributeDescription
                    = task.getModifiedAttributeDescription(taskEditDTO);
            notificationService.saveLogRecord(task,
                    modifiedAttributeDescription);

            modelMapper.map(taskEditDTO, task);
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(EntityDoesntExistException::new);
    }

    public void deleteById(Long id) {
        Task task = findById(id);
        for(LogRecord logRecord: task.getLog()){
            task.setLog(null);
            logRecord.setTask(null);
            logRepository.delete(logRecord);
        }

        taskRepository.deleteById(id);
    }

    public Task save(EditValueDTO editValueDTO) throws Exception {
        Task task;
        try {
            task = findById(editValueDTO.getId());
        } catch (Exception e) {
            throw new TaskDoesNotExistException();
        }
        Property property = propertyService.findById(editValueDTO.getValue().getProperty().getId());
        if (!task.getProject().getProperties().contains(property)) {
            throw new RuntimeException("There isn't a property with this id on the project : " + task.getProject().getName());
        }
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(task.getProject().getTeam().getId(), editValueDTO.getUserID());


        for (int i = 0; i < task.getValues().size(); i++) {
            if (task.getValues().get(i).getId().equals(editValueDTO.getValue().getId())) {
                Value currentValue = property.getKind().getValue();
                currentValue.setId(editValueDTO.getValue().getId());
                currentValue.setTask(task);
                currentValue.setProperty(property);
                if (property.getKind() == PropertyKind.STATUS) {
                    if (!userTeam.equals(task.getCreator()) && task.isRevisable()) {

                        // Validates another -> done
                        PropertyList propertyList = (PropertyList) editValueDTO.getValue().getValue();
                        if (propertyList.getPropertyListKind().equals(PropertyListKind.DONE)) {
                            throw new RuntimeException("Não é possível definir como concluído, " +
                                    "pois a tarefa deve passar por uma revisão do criador!");
                        }

                        // Validates done -> another
                        PropertyList propertyListCurrent = (PropertyList) task.getValues().get(i).getValue();
                        if (propertyListCurrent.getPropertyListKind().equals(PropertyListKind.DONE)) {
                            throw new RuntimeException("Apenas o criador da tarefa pode remover dos concluídos!");
                        }
                    }
                }
                currentValue.setValue(editValueDTO.getValue().getValue());
                task.getValues().set(i, currentValue);
                break;
            }
        }
        Task taskTest = taskRepository.save(task);

        //Notifications
        for (TaskResponsable taskResponsableFor : task.getTaskResponsables()) {
            if (!taskResponsableFor.getUserTeam().equals(userTeam) && taskResponsableFor.getUserTeam().getUser().getAnyUpdateOnTask()) {
                notificationService.save(new Notification(
                        task.getProject(),
                        "Valor da propriedade " + property.getName() + " alterado em " + taskTest.getName(),
                        "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                        taskResponsableFor.getUserTeam().getUser()
                ));
            }
        }

        String propertyValue = getPropertyValue(property, task);

        notificationService.saveLogRecord(task,
                ("O valor da propriedade "
                        + property.getName()
                        + " foi definido como "
                        + propertyValue));
        return taskTest;
    }

    private String getPropertyValue(Property property, Task task) {
        Value value = task.getValues()
                .stream()
                .filter(v -> Objects.equals(property.getId(), v.getProperty().getId()))
                .findFirst()
                .get();

        if (property.getKind() == PropertyKind.STATUS
                || property.getKind() == PropertyKind.LIST) {
            PropertyList pl = (PropertyList) value.getValue();
            return pl.getValue();
        }

        if (value instanceof ValueDate) {
            return ((ValueDate) value).format();
        }

        return value.getValue().toString();
    }

    //verify if the taskresponsable belongs to the task and if it is, save the comment
    public Task saveComment(CommentDTO commentDTO) {
        Task task;
        Comment comment = new Comment();
        TaskResponsable taskResponsable = taskResponsablesRepository.findById(commentDTO.getTaskResponsableID()).get();

        try {
            task = findById(commentDTO.getTaskID());
        } catch (Exception e) {
            throw new TaskDoesNotExistException();
        }

        if (taskResponsable.getTask().getId().equals(commentDTO.getTaskID())) {
            comment.setTask(task);
            comment.setTaskResponsable(taskResponsable);
            comment.setComment(commentDTO.getComment());
            comment.setDate(LocalDateTime.now());
            task.getComments().add(comment);

            //Notifications
            for (TaskResponsable taskResponsableFor : task.getTaskResponsables()) {
                if (!taskResponsableFor.equals(taskResponsable) && taskResponsableFor.getUserTeam().getUser().getAnyUpdateOnTask()) {
                    notificationService.save(new Notification(
                            task.getProject(),
                            "Novo comentário de " + taskResponsable.getUserTeam().getUser().getFullName() + " em " + task.getName(),
                            "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                            taskResponsableFor.getUserTeam().getUser()
                    ));
                }
            }

            notificationService.saveLogRecord(task,
                    "adicionou um comentário à tarefa",
                    taskResponsable.getUserTeam());

            return taskRepository.save(task);
        } else {
            throw new RuntimeException("O usuário não é um dos responsáveis pela tarefa.");
        }
    }

    public Boolean deleteComment(Long taskID, Long commentID) {
        Task task = findById(taskID);
        for (Comment comment : task.getComments()) {
            if (comment.getId().equals(commentID)) {
                task.getComments().remove(comment);
                taskRepository.save(task);
                return true;
            }
        }
        throw new RuntimeException("Comment doesn't exist in this task");
    }

    //add responsables to the task
    public Task saveResponsables(TaskResponsablesDTO taskResponsableDTO) {
        Task task;
        TaskResponsable taskResponsable;
        try {
            task = findById(taskResponsableDTO.getTask().getId());
        } catch (Exception e) {
            throw new TaskDoesNotExistException();
        }
        try {
            taskResponsableDTO.setUserTeam(userTeamService.findById(taskResponsableDTO.getUserTeam().getId()));
        } catch (Exception e) {
            throw new RuntimeException("Não há um usuário com esse id");
        }

        for (TaskResponsable taskResponsableFor : task.getTaskResponsables()) {
            if (taskResponsableFor.getId().equals(taskResponsableDTO.getId())) {
                if (taskResponsableFor.getUserTeam().getUser().getResponsibleInProjectOrTask()) {
                    notificationService.save(new Notification(
                            task.getProject(),
                            "Você não é mais responsável da tarefa " + task.getName(),
                            "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                            taskResponsableFor.getUserTeam().getUser()
                    ));
                }
                taskResponsable = taskResponsableFor;
                task.getTaskResponsables().remove(taskResponsable);
                return taskRepository.save(task);
            }
        }
        if (taskResponsableDTO.getId() == null) {
            taskResponsable = new TaskResponsable();
            BeanUtils.copyProperties(taskResponsableDTO, taskResponsable);
            taskResponsableDTO.setTask(task);
            task.getTaskResponsables().add(taskResponsable);

            //Notifications
            if (taskResponsable.getUserTeam().getUser().getResponsibleInProjectOrTask()) {
                notificationService.save(new Notification(
                        task.getProject(),
                        "Você foi adicionado como responsável da tarefa " + task.getName(),
                        "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                        taskResponsable.getUserTeam().getUser()
                ));
            }

            notificationService.saveLogRecord(task,
                    "adicionou um responsável à tarefa",
                    taskResponsable.getUserTeam());

            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Erro na exclusão de um participante");
        }
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }


    public List<Task> getAllByProject(Long id) {
        try {
            Project project = projectService.findById(id);
            return project.getTasks();

        } catch (Exception e) {
            throw new EntityNotFoundException();
        }

    }

    public TaskOpenDTO getTaskInfos(Long taskID) {
        Task task = findById(taskID);
        String fullName = task.getCreator().getUser().getFirstName() + " " + task.getCreator().getUser().getLastName();
        return new TaskOpenDTO(task.getProject().getTeam().getName()
                , task.getProject().getName()
                , fullName
                , task.getCreator().getUser().getEmail()
                , task.getProject().getProjectReviewENUM());
    }

    public List<Task> getAllByUser(Long userID) {
        try {
            List<UserTeam> uts = userTeamService.findAllUserTeamByUserId(userID);

            return uts.stream()
                    .flatMap(ut -> ut.getTeam()
                            .getProjects().stream()
                            .flatMap(p -> p.getTasks().stream()))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Task uploadFile(MultipartFile multipartFile, Long id, Long userThatSentID) {
        try {
            Task task = findById(id);
            File file = fileService.save(multipartFile, task);
            UserTeam ut = userTeamService
                    .findUserTeamByComposeId(
                            task.getProject().getTeam().getId(),
                            userThatSentID
                    );
            notificationService.saveLogRecord(task,
                    "adicionou um anexo à tarefa", ut);

            if (Objects.isNull(task.getFiles())) task.setFiles(List.of(file));
            else task.getFiles().add(file);

            //Notifications
            for (TaskResponsable taskResponsibleFor : task.getTaskResponsables()) {
                User user = taskResponsibleFor.getUserTeam().getUser();

                if (!user.getId().equals(userThatSentID)
                        && user.getAnyUpdateOnTask()) {

                    notificationService.save(new Notification(
                            task.getProject(),
                            "Novo anexo adicionado em " + task.getName(),
                            "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                            user
                    ));
                }
            }

            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Task removeFile(Long taskId, Long fileId) {
        try {
            Task task = findById(taskId);
            File file = fileService.findById(fileId);
            task.getFiles().remove(file);
            fileService.delete(fileId);
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<TaskSearchDTO> findAllByUserAndQuery(
            Long userId, String query) {

        return userTeamService.findAllUserTeamByUserId(userId)
                .stream()
                .map(UserTeam::getTeam)
                .flatMap(t -> t.getProjects().stream())
                .flatMap(p -> p.getTasks().stream())
                .filter(task -> task.getTaskResponsables()
                        .stream()
                        .anyMatch(tr -> tr.getUserTeam().getUser().getId().equals(userId))
                            && task.getName().toLowerCase().contains(query.toLowerCase()))
                .map(TaskSearchDTO::new)
                .toList();
    }

    public Task setAsDone(Task task) {
        for (Value value : task.getValues()) {
            if (value.getProperty().getKind().equals(PropertyKind.STATUS)) {
                Optional<PropertyList> doneDefault =
                        value.getProperty().getPropertyLists()
                                .stream()
                                .filter(propertyList ->
                                        propertyList.getIsFixed() &&
                                                propertyList.getPropertyListKind().equals(PropertyListKind.DONE))
                                .findFirst();
                doneDefault.ifPresent(value::setValue);
            }
        }
        return save(task);
    }

    public Task editTaskResponsables(UpdateTaskResponsableDTO updateTaskResponsableDTO) {
        Task task = findById(updateTaskResponsableDTO.getTaskId());
        List<TaskResponsable> responsablesToDelete = new ArrayList<>();
        List<Group> groupsToDelete = new ArrayList<>();
        boolean canDeleteUser = false;
        boolean canDeleteGroup = false;

        if (task.getTaskResponsables().isEmpty()) {
            UserTeam userTeam = userTeamService.findUserTeamByComposeId(updateTaskResponsableDTO.getTeamId(), updateTaskResponsableDTO.getUser().getId());
            TaskResponsable taskResponsable1 = new TaskResponsable(userTeam, task);
            taskResponsablesRepository.save(taskResponsable1);
        }

        for (TaskResponsable taskResponsable : task.getTaskResponsables()) {
            if (updateTaskResponsableDTO.getUser() != null) {
                if (taskResponsable.getUserTeam().getUser().getId().equals(updateTaskResponsableDTO.getUser().getId())) {
                    responsablesToDelete.add(taskResponsable);
                    canDeleteUser = true;
                }
            }
        }

        if (updateTaskResponsableDTO.getGroup() != null) {
            for (Group group : task.getGroups()) {
                if (group.getId().equals(updateTaskResponsableDTO.getGroup().getId())) {
                    groupsToDelete.add(group);
                    canDeleteGroup = true;
                }
            }
        }

        if (!canDeleteUser && !canDeleteGroup) {
            if (updateTaskResponsableDTO.getGroup() != null) {
                task.getGroups().add(updateTaskResponsableDTO.getGroup());
            } else {
                UserTeam userTeam = userTeamService.findUserTeamByComposeId(updateTaskResponsableDTO.getTeamId(), updateTaskResponsableDTO.getUser().getId());
                TaskResponsable taskResponsable1 = new TaskResponsable(userTeam, task);
                taskResponsablesRepository.save(taskResponsable1);
            }
        }

        for (TaskResponsable taskResponsable : responsablesToDelete) {
            task.getTaskResponsables().remove(taskResponsable);
            taskResponsable.setTask(null);
            taskResponsablesRepository.delete(taskResponsable);
        }
        for (Group group : groupsToDelete) {
            task.getGroups().remove(group);
            group.setTasks(null);
        }

        return taskRepository.save(task);
    }

    public Task setDependency(Long taskId, Long taskDependencyId) {
        Task task = findById(taskId);
        Task tDependency = findById(taskDependencyId);
        if (tDependency.getTaskDependency() != null) {
            if (tDependency.getTaskDependency().getId().equals(taskId)) {
                throw new RuntimeException("A outra tarefa já está associada a essa");
            }
        } else {
            Task taskDependency = findById(taskDependencyId);
            task.setTaskDependency(taskDependency);
            return taskRepository.save(task);
        }
        return null;
    }

    public void setDependencyByTask(Long taskId) {
        Task task = findById(taskId);
        for (Task taskDependents : taskRepository.findAll()) {
            if (taskDependents.getTaskDependency() != null) {
                if (task.getId().equals(taskDependents.getTaskDependency().getId())) {
                    taskDependents.setTaskDependency(null);
                    taskRepository.save(taskDependents);
                }
            }
        }
    }

    public List<Permission> getTaskPermissions(Long taskID, Long userID){
        Task task = findById(taskID);
        //Get all users teams of an user
        for (UserTeam userteamFor : userTeamService.findAllUserTeamByUserId(userID)){
            if(userteamFor.getTeam().equals(task.getProject().getTeam())){
                return  userteamFor.getPermissionUser();
            }
        }
        throw new UserNotFoundException();
    }

    public boolean getTasksDone(Long projectId){
        Project project = projectService.findById(projectId);
        //[0] - TODO
        //[1] - DOING
        //[2] - DONE
        List<Integer> tasksCategory = new ArrayList<>();
        List<PropertyListKind> listKinds = List.of(PropertyListKind.TODO, PropertyListKind.DOING, PropertyListKind.DONE);

        for (PropertyListKind propertyListKind : listKinds) {
            tasksCategory.add(project.getTasks().stream()
                    .flatMap(t -> t.getValues().stream())
                    .filter(value -> value.getProperty().getKind().equals(PropertyKind.STATUS)
                            && ((PropertyList) value.getValue()).getPropertyListKind().equals(propertyListKind))
                    .toList().size());
        }

        return tasksCategory.get(2) == project.getTasks().size();
    }

    public ReturnTaskResponsablesDTO returnAllResponsables(Long taskId){
        ReturnTaskResponsablesDTO returnTaskResponsablesDTO = new ReturnTaskResponsablesDTO();
        List<User> users = new ArrayList<>();
        Task task = findById(taskId);

        for(TaskResponsable taskResponsable: task.getTaskResponsables()){
            users.add(taskResponsable.getUserTeam().getUser());
        }

        returnTaskResponsablesDTO.setGroups(new ArrayList<>(task.getGroups()));
        returnTaskResponsablesDTO.setUsers(users);
        return returnTaskResponsablesDTO;
    }


}
