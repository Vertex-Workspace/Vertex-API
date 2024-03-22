package com.vertex.vertex.task.relations.review.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
public class SendToReviewDTO {

    private String description;

    private TaskResponsable userThatSentReview;

    private Task task;
}
