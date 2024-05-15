package com.vertex.vertex.task.relations.value.model.entity;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueList extends Value {

    @ManyToOne
    private PropertyList value;

    @Override
    public void setValue(Object object) {
        this.value = (PropertyList) object;
    }

    public ValueList(Long id, Task task, Property property, PropertyList value) {
        super(id, task, property);
        this.value = value;
    }

}
