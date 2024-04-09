package com.vertex.vertex.task.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.model.DTO.TaskOpenDTO;
import com.vertex.vertex.task.model.DTO.TaskSearchDTO;
import com.vertex.vertex.task.relations.review.repository.ReviewRepository;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.TaskDoesNotExistException;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
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
    private final ValueService valueService;


    public Task save(TaskCreateDTO taskCreateDTO) {
        Project project = projectService.findById(taskCreateDTO.getProject().getId());

        UserTeam creator = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), taskCreateDTO.getCreator().getId());
        //create, copy attributes, set if is revisable, set creator and 1st responsible and start log
        Task task = new Task(taskCreateDTO, project, creator);

        //When the task is created, every property is associated with a null value, unless it has a default value
        valueService.setTaskDefaultValues(task, project.getProperties());

        //Notifications and task log
        for (TaskResponsable taskResponsable : task.getTaskResponsables()) {
            if (taskResponsable.getUserTeam().getUser().getResponsibleInProjectOrTask() && !taskResponsable.getUserTeam().equals(task.getCreator())) {
                notificationService.save(new Notification(
                        project,
                        "Você foi adicionado como responsável da tarefa " + task.getName(),
                        "projeto/" + project.getId() + "/tarefas?taskID=" + task.getId(),
                        taskResponsable.getUserTeam().getUser()
                ));
            }
            notificationService.saveLogRecord(task,
                    "foi adicionado à lista de responsáveis pela tarefa",
                    taskResponsable.getUserTeam());
        }

        return task;
    }

    public Task edit(TaskEditDTO taskEditDTO) {
        try {
            Task task = findById(taskEditDTO.getId());

            notificationService.saveLogRecord(task,
                    task.getModifiedAttributeDescription(taskEditDTO));

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
        Optional<Task> t = taskRepository.findById(id);

        if (t.isPresent()) {
            return t.get();
        }

        throw new TaskDoesNotExistException();
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task save(EditValueDTO editValueDTO) throws Exception {
        Task task = findById(editValueDTO.getId());

        Property property = propertyService.findByIdAndProjectContains(
                editValueDTO.getValue().getProperty().getId(), task.getProject());

        UserTeam userTeam = userTeamService.findUserTeamByComposeId(
                task.getProject().getTeam().getId(), editValueDTO.getUserID());

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

        return taskRepository.save(task);
    }


    //verify if the taskresponsable belongs to the task and if it is, save the comment
    public Task saveComment(CommentDTO commentDTO) {
        TaskResponsable taskResponsable = taskResponsablesRepository.findById(commentDTO.getTaskResponsableID()).get();
        Task task = findById(commentDTO.getTaskID());

        if (taskResponsable.getTask().getId().equals(commentDTO.getTaskID())) {
            new Comment(commentDTO, task, taskResponsable);

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
        Task task = findById(taskResponsableDTO.getTask().getId());
        taskResponsableDTO.setUserTeam(userTeamService.findById(taskResponsableDTO.getUserTeam().getId()));

        //update responsibles, send notifications and return saved task
        return updateResponsiblesSendNotifications(taskResponsableDTO, task);
    }

    private Task updateResponsiblesSendNotifications(TaskResponsablesDTO taskResponsableDTO, Task task) {
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
                task.getTaskResponsables().remove(taskResponsableFor);
                return save(task);
            }
        }
        if (taskResponsableDTO.getId() == null) {
            TaskResponsable taskResponsable = new TaskResponsable(taskResponsableDTO, task);

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

            return save(task);
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

    public List<Task> getAllByUser(Long id) {
        try {
            List<UserTeam> uts = userTeamService.findAll(id);

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
            fileService.save(multipartFile, task);
            UserTeam ut = userTeamService.findUserTeamByComposeId(
                    task.getProject().getTeam().getId(),
                    userThatSentID
            );
            notificationService.saveLogRecord(task, "adicionou um anexo à tarefa", ut);

            //Notifications
            for (TaskResponsable taskResponsibleFor : task.getTaskResponsables()) {
                User user = taskResponsibleFor.getUserTeam().getUser();

                if (!user.getId().equals(userThatSentID) && user.getAnyUpdateOnTask()) {
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

    public List<TaskSearchDTO> findAllByUserAndQuery(Long userId, String query) {
        return userTeamService.findAllByUser(userId)
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

}
