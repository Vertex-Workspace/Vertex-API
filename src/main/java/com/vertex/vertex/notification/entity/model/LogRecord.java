package com.vertex.vertex.notification.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDateTime date;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Task task;

    public LogRecord(Task task, String description, UserTeam ut) {
        this.task = task;

        String name = ut.getUser().getFullName();
        this.description = name + " " + description;
        this.date = LocalDateTime.now();

        if (Objects.isNull(task.getLog())) task.setLog(List.of(this));
        else task.getLog().add(this);
    }

    public LogRecord(Task task, String description) {
        this.task = task;
        this.description = description;
        this.date = LocalDateTime.now();

        if (Objects.isNull(task.getLog())) task.setLog(List.of(this));
        else task.getLog().add(this);
    }

}
