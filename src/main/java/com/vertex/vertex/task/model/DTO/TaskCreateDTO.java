package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskCreateDTO {

    private Long id;
    private String name;
    private User creator;
    private Long teamId;
    private List<TaskResponsable> responsables;
    private String description;
    private List<Comment> comments;
    private Task taskDependency;
    private Project project;
    private List<Task> subTasks;
    private List<Review> reviews;
    private List<Value> values;

    public TaskCreateDTO(String name, String description, User creator, Project project) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.project = project;
    }

}
