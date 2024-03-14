package com.vertex.vertex.task.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewHoursDTO;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewSenderDTO;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskWaitingToReviewDTO {
    private Long id;
    private String name;
    private List<ReviewHoursDTO> reviewHours;
    private String description;
    private ReviewSenderDTO sender;


    public TaskWaitingToReviewDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
    }
}
