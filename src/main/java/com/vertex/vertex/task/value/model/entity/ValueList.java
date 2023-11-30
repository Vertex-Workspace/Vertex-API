package com.vertex.vertex.task.value.model.entity;

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

    @ManyToOne
    private PropertyList value;

    @Override
    public void setValue(Object object) {
        this.value = (PropertyList) object;
    }
}
