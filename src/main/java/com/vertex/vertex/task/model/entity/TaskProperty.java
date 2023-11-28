package com.vertex.vertex.task.model.entity;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.value.model.entity.Value;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Task task;

    @ManyToOne
    private Property property;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taskProperty")
    private Value value;
}
