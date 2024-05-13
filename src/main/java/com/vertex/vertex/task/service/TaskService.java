package com.vertex.vertex.task.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.repository.LogRepository;
import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.security.ValidationUtils;
import com.vertex.vertex.task.model.DTO.*;
import com.vertex.vertex.task.relations.review.repository.ReviewRepository;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.TaskDoesNotExistException;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
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
import java.util.*;

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
    private final ValueService valueService;
    private final NotificationService notificationService;


    public TaskModeViewDTO save(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProject().getId());


        UserTeam creator = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), taskCreateDTO.getCreator().getId());
        if(!creator.getUser().isDefaultSettings()){
            ValidationUtils.validateUserLogged(creator.getUser().getEmail());
        }

        if(!Permission.hasPermission(creator.getPermissionUser(), TypePermissions.Criar)){
            throw new RuntimeException("Você não tem permissão!");
        }
        long index = 0L;
        List<Long> indexs = new ArrayList<>(project.getTasks().stream().map(Task::getIndexTask).toList());

        if(indexs.isEmpty()){
            index = 1L;
        } else {
            Collections.sort(indexs);
            index = indexs.get(indexs.size()-1) + 1;
        }

        Task task = new Task(taskCreateDTO, project, creator);
        setResponsablesInTask(project, task);
        task.setIndexTask(index);

        //When the task is created, every property is associated with a null value, unless it has a default value
        valueService.setTaskDefaultValues(task, project.getProperties());

        //Notifications
        for (TaskResponsable taskResponsable : task.getTaskResponsables()) {
            if (taskResponsable.getUserTeam().getUser().getResponsibleInProjectOrTask() && !taskResponsable.getUserTeam().equals(task.getCreator())) {
                notificationService.save(new Notification(
                        project,
                        "Você foi adicionado como responsável da tarefa " + task.getName(),
                        "projeto/" + project.getId() + "/tarefas?taskID=" + task.getId(),
                        taskResponsable.getUserTeam().getUser()
                ));
            }
        }

        return new TaskModeViewDTO(save(task));
    }
    public void setResponsablesInTask(Project project, Task task){
        List<TaskResponsable> taskResponsables = new ArrayList<>();
        for(UserTeam userTeam : project.getCollaborators()){
            taskResponsables.add(new TaskResponsable(userTeam, task));
        }
        task.setTaskResponsables(taskResponsables);
    }



    public Task edit(TaskEditDTO taskEditDTO) {
        try {
            Task task = findById(taskEditDTO.getId());

            validateUserLoggedIntoTask(task);
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

    public Task findById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            validateUserLoggedIntoTask(task);
            return task;
        }

        throw new TaskDoesNotExistException();
    }
    private void validateUserLoggedIntoTask(Task task){
        ValidationUtils.validateUserLogged(
                task.getTaskResponsables()
                        .stream()
                        .map(TaskResponsable::getUserTeam)
                        .map(UserTeam::getUser)
                        .map(User::getEmail).toList());
    }

    public void deleteById(Long id) {
        Task task = findById(id);
        ValidationUtils.loggedUserIsOnTaskAndIsCreator(task);
        taskRepository.deleteById(id);
    }

    public Task save(EditValueDTO editValueDTO) throws Exception {
        Task task = findById(editValueDTO.getId());

        Property property = propertyService.findByIdAndProjectContains(
                editValueDTO.getValue().getProperty().getId(), task.getProject());

        UserTeam userTeam = userTeamService.findUserTeamByComposeId(
                task.getProject().getTeam().getId(), editValueDTO.getUserID());
        ValidationUtils.validateUserLogged(userTeam.getUser().getEmail());
        //update the specific value, validate rules and save
        save(valueService.updateTaskValues(task, editValueDTO, property, userTeam));

        //Notifications
        for (TaskResponsable taskResponsableFor : task.getTaskResponsables()) {
            if (!taskResponsableFor.getUserTeam().equals(userTeam) && taskResponsableFor.getUserTeam().getUser().getAnyUpdateOnTask()) {
                notificationService.save(new Notification(
                        task.getProject(),
                        "Valor da propriedade " + property.getName() + " alterado em " + task.getName(),
                        "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                        taskResponsableFor.getUserTeam().getUser()
                ));
            }
        }

        //find the specific value inside the task and return the final value as string
        String propertyValue = propertyService.getPropertyValueAsString(property, task);
        notificationService.saveLogRecord(task,
                ("O valor da propriedade " + property.getName()
                        + " foi definido como " + propertyValue));
        return task;
    }


    //verify if the taskresponsable belongs to the task and if it is, save the comment
    public Task saveComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        TaskResponsable taskResponsable = taskResponsablesRepository.findById(commentDTO.getTaskResponsableID()).get();
        Task task = findById(commentDTO.getTaskID());
        ValidationUtils.validateUserLogged(taskResponsable.getUserTeam().getUser().getEmail());

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

        ValidationUtils.loggedUserIsOnTask(task);

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
        ValidationUtils.loggedUserIsOnTask(task);
        String fullName = task.getCreator().getUser().getFirstName() + " " + task.getCreator().getUser().getLastName();
        return new TaskOpenDTO(task.getProject().getTeam().getName()
                , task.getProject().getName()
                , fullName
                , task.getCreator().getUser().getEmail()
                , task.getProject().getProjectReviewENUM());
    }

    public List<TaskModeViewDTO> getAllByUser(Long userID) {
        try {
            return userTeamService.findAllUserTeamByUserId(userID)
                    .stream()
                    .flatMap(ut -> ut.getTeam().getProjects().stream()
                            .flatMap(p -> filterTasksByResponsible(p.getTasks(), ut).stream())
                    )
                    .map(TaskModeViewDTO::new)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    private List<Task> filterTasksByResponsible(List<Task> tasks, UserTeam userTeam){
        return tasks.stream()
                .flatMap(task -> task.getTaskResponsables()
                        .stream()
                        .filter(tr -> tr.getUserTeam().equals(userTeam))
                ).map(TaskResponsable::getTask)
                .toList();
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

            ValidationUtils.validateUserLogged(ut.getUser().getEmail());
            task.getFiles().add(file);

            notificationService.saveLogRecord(task, "adicionou um anexo à tarefa", ut);

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
            ValidationUtils.loggedUserIsOnTask(task);
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

        ValidationUtils.loggedUserIsOnTaskAndIsCreator(task);

        List<TaskResponsable> responsablesToDelete = new ArrayList<>();
        List<Group> groupsToDelete = new ArrayList<>();
        boolean canDeleteUser = false;
        boolean canDeleteGroup = false;

        if (task.getTaskResponsables().isEmpty()) {
            Optional<UserTeam> userTeam = task.getProject()
                    .getCollaborators()
                    .stream()
                    .filter(ut -> ut.getUser().getId().equals(updateTaskResponsableDTO.getUser().getId()))
                    .findAny();
            userTeam.ifPresent(team -> taskResponsablesRepository.save(new TaskResponsable(team, task)));
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
                Optional<UserTeam> userTeam = task.getProject()
                        .getCollaborators()
                        .stream()
                        .filter(ut -> ut.getUser().getId().equals(updateTaskResponsableDTO.getUser().getId()))
                        .findAny();
                userTeam.ifPresent(team -> taskResponsablesRepository.save(new TaskResponsable(team, task)));
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
        ValidationUtils.loggedUserIsOnTaskAndIsCreator(task);

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

    public void setTaskDependencyNull(Long taskId) {
        Task task = findById(taskId);
        ValidationUtils.loggedUserIsOnTaskAndIsCreator(task);
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
        ValidationUtils.loggedUserIsOnTask(task);

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
