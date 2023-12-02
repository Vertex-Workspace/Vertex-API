package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.EditValueDTO;
import com.vertex.vertex.task.model.DTO.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewDTO;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.team.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
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
        }catch(Exception e){
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
            throw new RuntimeException("There isn't a task with this id!");
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

    public Task saveComment(CommentDTO commentDTO) {
        Task task;
        Comment comment = new Comment();
        try {
            task = findById(commentDTO.getId());
        }catch(Exception e){
            throw new RuntimeException("Não existe uma tarefa com esse id para postar um comentário");
        }
        BeanUtils.copyProperties(commentDTO, comment);
        task.getComments().add(comment);
        return taskRepository.save(task);
    }

    public Task saveReview(ReviewDTO reviewDTO) {
        Task task = findById(reviewDTO.getId());
        Review review = new Review();
        TaskResponsable taskResponsable = task.getCreator();
            if(reviewDTO.getReviewer().getId().equals(taskResponsable.getId())){
                BeanUtils.copyProperties(reviewDTO, review);
                task.getReviews().add(review);
                return taskRepository.save(task);
            }
        throw new RuntimeException("O avaliador não é o criador da tarefa");

    }

    public Task saveResponsables(TaskResponsable taskResponsable){
        Task task;
        try {
            task = findById(taskResponsable.getTask().getId());
        }catch(Exception e){
            throw new RuntimeException("Não há uma tarefa com esse id");
        }
        taskResponsable.setTask(task);
        taskResponsable.setUserTeam(userTeamService.findById(taskResponsable.getUserTeam().getId()));
        task.getTaskResponsables().add(taskResponsable);
        return taskRepository.save(task);
    }
}
