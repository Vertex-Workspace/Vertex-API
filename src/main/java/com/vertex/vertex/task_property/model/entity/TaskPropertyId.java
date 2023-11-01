package com.vertex.vertex.task_property.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskPropertyId implements Serializable {
    @Id
    private Long taskId;
    @Id
    private Long propertyId;
}
