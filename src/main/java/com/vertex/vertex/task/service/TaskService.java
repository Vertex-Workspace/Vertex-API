package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.TaskDoesNotExistException;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewDTO;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.ValueList;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    public void save(TaskCreateDTO taskCreateDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);
        Project project;
        TaskResponsable taskResponsable = new TaskResponsable();
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
            task.getValues().add(currentValue);
        }
        //set the creator of the task
        try {
            taskResponsable.setUserTeam(userTeamService.findById(taskCreateDTO.getCreator().getUserTeam().getId()));
            taskResponsable.setTask(task);
            task.setCreator(taskResponsable);
        } catch (Exception e) {
            throw new RuntimeException("Não foi encontrado o usuário para ele ser o criador da tarefa");
        }
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public void deleteById(Long id) {
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
            }
        }
        return taskRepository.save(task);
    }

    //verify if the taskresponsable belongs to the task and if it is, save the comment
    public Task saveComment(CommentDTO commentDTO) {
        Task task;
        Comment comment;
        TaskResponsable taskResponsable = taskResponsablesRepository.findById(commentDTO.getTaskResponsable().getId()).get();
        try {
            task = findById(commentDTO.getTask().getId());
        } catch (Exception e) {
            throw new TaskDoesNotExistException();
        }

        if (commentDTO.getId() != null) {
            for (Comment commentFor : task.getComments()) {
                if (commentFor.getId().equals(commentDTO.getId())) {
                    comment = commentFor;
                    task.getComments().remove(comment);
                    return taskRepository.save(task);
                }
            }
        } else {
            comment = new Comment();
            if (taskResponsable.getTask().getId().equals(commentDTO.getTask().getId())) {
                BeanUtils.copyProperties(commentDTO, comment);
                task.getComments().add(comment);
            } else {
                throw new RuntimeException("O usuário não é um dos responsáveis pela tarefa.");
            }
        }
        return taskRepository.save(task);
    }

    //verify if the reviewer is the creator of the task
    public Task saveReview(ReviewDTO reviewDTO) {
        Task task = findById(reviewDTO.getTask().getId());
        Review review;
        TaskResponsable taskResponsable = taskResponsablesRepository.findById(reviewDTO.getReviewer().getId()).get();

        if (reviewDTO.getId() != null) {
            for (Review reviewFor : task.getReviews()) {
                if (reviewFor.getId().equals(reviewDTO.getId())) {
                    review = reviewFor;
                    task.getReviews().remove(review);
                    return taskRepository.save(task);
                }
            }
        } else {
            review = new Review();
            if (task.getCreator().getId().equals(taskResponsable.getId())) {
                BeanUtils.copyProperties(reviewDTO, review);
                approveReview(review);
                task.getReviews().add(review);
            } else {
                throw new RuntimeException("O usuário não é o criador da tarefa");
            }
        }
        return taskRepository.save(task);
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

        for (TaskResponsable taskResponsable1 : task.getTaskResponsables()) {
            if (taskResponsable1.getId().equals(taskResponsableDTO.getId())) {
                taskResponsable = taskResponsable1;
                task.getTaskResponsables().remove(taskResponsable);
                System.out.println(taskResponsable);
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

    //method to change the status of the task that was revised
    public void approveReview(Review review) {
        Task task = findById(review.getTask().getId());
        System.out.println(task);
        for (int i = 0; i < task.getValues().size(); i++) {
            PropertyKind propertyKind = task.getValues().get(i).getProperty().getKind();
            if (propertyKind == PropertyKind.STATUS) {
                if (review.getApproved()) {
                    System.out.println(task.getValues());
                    task.getValues();
                }else{
                    propertyKind.getValue().setValue("UNDER ANALYSIS");
                }
            }
        }
    }
}
