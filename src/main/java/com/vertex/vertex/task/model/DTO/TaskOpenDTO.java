package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

//This DTO is use when the user opens the task

//is missing log of fetches at task
public class TaskOpenDTO {

    private String name;
    private String description;
    private List<TaskResponsable> responsables;
    private List<Comment> comments;
    private Task taskDependency;
    //future implementation
    private List<Task> subTasks;

}
