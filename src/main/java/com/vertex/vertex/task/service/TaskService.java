package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.model.DTO.TaskOpenDTO;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.TaskDoesNotExistException;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.relations.value.model.entity.ValueNumber;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskResponsablesRepository taskResponsablesRepository;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final PropertyService propertyService;
    private final UserTeamService userTeamService;


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
            if(property.getKind() == PropertyKind.TEXT){
                currentValue.setValue(property.getDefaultValue());
            }
        }
        task.setValues(values);

        //Add the taskResponsables on task list of taskResponsables
        task.setCreator(userTeamService.findById(taskCreateDTO.getCreator().getId()));
        try {
            for (UserTeam userTeam : project.getTeam().getUserTeams()) {
                TaskResponsable taskResponsable1 = new TaskResponsable(userTeam, task);
                if (task.getTaskResponsables() == null) {
                    ArrayList<TaskResponsable> listaParaFuncionarEstaCoisaBemLegal = new ArrayList<>();
                    listaParaFuncionarEstaCoisaBemLegal.add(taskResponsable1);
                    task.setTaskResponsables(listaParaFuncionarEstaCoisaBemLegal);
                } else {
                    task.getTaskResponsables().add(taskResponsable1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        task.setApproveStatus(ApproveStatus.INPROGRESS);
        return taskRepository.save(task);
    }


    public Task edit(TaskEditDTO taskEditDTO) {
        try {
            Task task = findById(taskEditDTO.getId());
            BeanUtils.copyProperties(taskEditDTO, task);
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        Task task = findById(id);
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
        for (int i = 0; i < task.getValues().size(); i++) {
            if (task.getValues().get(i).getId().equals(editValueDTO.getValue().getId())) {
                Value currentValue = property.getKind().getValue();
                currentValue.setId(editValueDTO.getValue().getId());
                currentValue.setTask(task);
                currentValue.setProperty(property);
                currentValue.setValue(editValueDTO.getValue().getValue());
                task.getValues().set(i, currentValue);
                task.setApproveStatus(ApproveStatus.INPROGRESS);
            }
        }
        return taskRepository.save(task);
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
        } else {
            throw new RuntimeException("Erro na exclusão de um participante");
        }
        return taskRepository.save(task);
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
                , task.getCreator().getUser().getEmail());
    }

    public List<Task> getAllByUser(Long id) {
        try {
            List<UserTeam> uts = userTeamService.findAll(id);
            List<Task> taskList = new ArrayList<>();

            uts.forEach(ut -> {
                ut.getTeam().getProjects().forEach(p -> {
                    taskList.addAll(p.getTasks());
                });
            });

            return taskList;

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



}
