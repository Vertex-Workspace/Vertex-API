package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.relations.comment.model.Comment;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TaskCreateDTO {

    private Long id;
    private String name;
    private List<UserTeam> responsables;
    private String description;
    private List<Comment> comments;
    private Task taskDependency;
    private Project project;
    private List<Task> subTasks;
    private List<Review> reviews;
    private List<Value> values;
}
