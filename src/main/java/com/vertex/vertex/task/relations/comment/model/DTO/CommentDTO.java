package com.vertex.vertex.task.relations.comment.model.DTO;

import com.vertex.vertex.team.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class CommentDTO {

    //task id
    private Long id;
    private String comment;
    private Date date;
    private TaskResponsable taskResponsable;
}
