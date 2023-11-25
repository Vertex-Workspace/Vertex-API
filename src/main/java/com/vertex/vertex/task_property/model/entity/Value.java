package com.vertex.vertex.task_property.model.entity;

import com.vertex.vertex.property.model.entity.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private com.vertex.vertex.task.model.entity.TaskProperty task;

    @OneToOne
    private Property property;

    private String value;
}
