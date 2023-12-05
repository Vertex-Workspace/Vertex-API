package com.vertex.vertex.task.relations.review.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Date date;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private TaskResponsable reviewer;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;

    private Double grade;

    private Boolean approved;

}
