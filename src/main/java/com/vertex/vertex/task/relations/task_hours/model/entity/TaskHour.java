package com.vertex.vertex.task.relations.task_hours.model.entity;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date initialDate;

    private Date finalDate;

    @ManyToOne
    @NotNull
    private Task task;

    @ManyToOne
    @NotNull
    private UserTeam userTeam;

}
