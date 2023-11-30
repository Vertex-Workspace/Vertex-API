package com.vertex.vertex.task.relations.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueText extends Value {

    private String value;

    @Override
    public void setValue(Object object) {
        this.value = (String) object;
    }

}
