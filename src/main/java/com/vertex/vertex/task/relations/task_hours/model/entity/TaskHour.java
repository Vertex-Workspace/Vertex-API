package com.vertex.vertex.task.relations.task_hours.model.entity;


import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
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

    @ManyToOne
    private TaskResponsable taskResponsable;

}
