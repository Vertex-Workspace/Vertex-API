package com.vertex.vertex.task.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ValueText extends Value {

    private String value;

    public ValueText(Long id, String value) {
        super(id);
        this.value = value;
    }

    public ValueText(String value) {
        this.value = value;
    }

    @Override
    public void setValue(Object object) {
        this.value = (String) object;
    }

}
