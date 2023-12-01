package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.EditValueDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.NotFoundPropertyInTaskException;
import com.vertex.vertex.task.model.exceptions.NotFoundValueInListException;
import com.vertex.vertex.task.relations.comment.model.Comment;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
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

    public Task save(TaskCreateDTO taskCreateDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);
        Project project;
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
        return taskRepository.save(task);
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

    public Task saveComment(Comment comment) {
        Task task;
        try {
            task = findById(comment.getTask().getId());
        }catch(Exception e){
            throw new RuntimeException("Não existe uma tarefa com esse id para postar um comentário");
        }
        task.getComments().add(comment);
        return taskRepository.save(task);
    }

    public Task saveReview(Review review) {
        Task task = findById(review.getTask().getId());
        UserTeam userTeam = userTeamService.findById(review.getReviewer().getId());
        for (int i = 0; i < task.getResponsables().size(); i++) {
            if(task.getResponsables().get(i).getId().equals(userTeam.getId())){
                task.getReviews().add(review);
                return taskRepository.save(task);
            }
        }
        throw new RuntimeException("Quem revisou a tarefa não faz parte da mesma equipe");
    }
}
