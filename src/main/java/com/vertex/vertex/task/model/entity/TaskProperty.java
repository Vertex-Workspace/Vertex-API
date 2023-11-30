package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.value.model.entity.Value;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = TaskPropertyDeserializer.class)
public class TaskProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;

    @ManyToOne
    private Property property;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taskProperty")
    private Value value;

}
