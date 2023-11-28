package com.vertex.vertex.task_property.value.model.entity;

import com.vertex.vertex.property.model.entity.PropertyList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueList extends Value {

    @OneToOne
    private PropertyList propertyList;

}
