package com.vertex.vertex.task.relations.comment.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private LocalDateTime date;

    @ManyToOne
    @ToString.Exclude
    private TaskResponsable taskResponsable;
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;

    public Comment(CommentDTO dto, Task task, TaskResponsable tr) {
        this.task = task;
        this.taskResponsable = tr;
        this.comment = dto.getComment();
        this.date = LocalDateTime.now();

        if (Objects.isNull(task.getComments())) task.setComments(List.of(this));
        else task.getComments().add(this);
    }

}
