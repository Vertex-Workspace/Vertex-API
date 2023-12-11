package com.vertex.vertex.task.relations.value.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueNumber extends Value {

    private Long value;

    @Override
    public void setValue(Object object) {
        String objectS = (String) object;
        this.value = Long.parseLong(objectS);
//        this.value = Long.valueOf(objectS).longValue();
//        this.value = new Long(objectS).longValue();
//        new Long(myString).longValue();
        //Long.valueOf(myString).longValue();
    }
}
