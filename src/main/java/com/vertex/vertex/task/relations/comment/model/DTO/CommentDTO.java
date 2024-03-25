package com.vertex.vertex.task.relations.comment.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private Long taskID;
    private String comment;
    private LocalDateTime date;
    private Long taskResponsableID;
}
