package com.vertex.vertex.task.relations.review.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String finalDescription;

    private LocalDateTime sentDate;

    private LocalDateTime reviewDate;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    //Can be null
    private TaskResponsable creatorReviewer;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private TaskResponsable userThatSentReview;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;

    //Can be null
    private Double grade;

    @Enumerated(value = EnumType.STRING)
    private ApproveStatus approveStatus;

}
