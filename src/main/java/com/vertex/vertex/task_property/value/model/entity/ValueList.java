package com.vertex.vertex.task_property.value.model.entity;

import com.vertex.vertex.property_list.model.entity.PropertyList;
import com.vertex.vertex.task_property.model.entity.TaskProperty;
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
