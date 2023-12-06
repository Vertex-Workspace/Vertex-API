package com.vertex.vertex.task.relations.review.model.DTO;

import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReviewCheck {

    private String description;
    private TaskResponsable reviewer;
}
