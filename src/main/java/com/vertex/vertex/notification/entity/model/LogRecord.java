package com.vertex.vertex.notification.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.model.entity.Task;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Task task;

}
