package com.vertex.vertex.task.relations.comment.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private Task task;
    private String comment;
    private Date date;
    private TaskResponsable taskResponsable;
}
