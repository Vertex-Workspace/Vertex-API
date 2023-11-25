package com.vertex.vertex.task.review.model.entity;

import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UserTeam reviewer;

    @ManyToOne
    private TaskProperty task;

    private Double grade;

    private Boolean approved;

}
