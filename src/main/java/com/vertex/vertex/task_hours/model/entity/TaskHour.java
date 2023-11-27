package com.vertex.vertex.task_hours.model.entity;

import com.vertex.vertex.task.model.entity.Task;
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
public class TaskHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date initialDate;

    private Date finalDate;

    @OneToOne
    private Task task;

    @OneToOne
    private UserTeam userTeam;

}
