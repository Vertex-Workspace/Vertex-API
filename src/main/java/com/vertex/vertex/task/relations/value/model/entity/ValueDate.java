package com.vertex.vertex.task.relations.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueDate extends Value {

    private LocalDateTime value;

    @Override
    public void setValue(Object object) {
        String objectS = (String) object;
        this.value = LocalDateTime.parse(objectS);
    }
}