package com.vertex.vertex.task.relations.review.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class ReviewDTO {

    //task id
    private Long id;
    private String description;
    private Date date;
    private TaskResponsable reviewer;
    private Double grade;
    private Boolean approved;
}
