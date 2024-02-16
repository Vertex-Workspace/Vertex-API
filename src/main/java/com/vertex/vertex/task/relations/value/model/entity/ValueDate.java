package com.vertex.vertex.task.relations.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueDate extends Value {

    private LocalDateTime value;

    @Override
    public void setValue(Object object) {
        this.value = LocalDateTime.parse(object.toString());
    }

    public void setValue(){
        this.value = LocalDateTime.now();
    }
}
