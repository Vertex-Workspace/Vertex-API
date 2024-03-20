package com.vertex.vertex.task.relations.review.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
public class ReviewCheck {
    private Long taskID;
    private Long reviewerID;
    private Double grade;
    private String finalDescription;
    private ApproveStatus approveStatus;
}
