package com.vertex.vertex.task.relations.review.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
public class SendReview {

    private Long id;
    private Task task;
    private String description;
    private Date date;
}
