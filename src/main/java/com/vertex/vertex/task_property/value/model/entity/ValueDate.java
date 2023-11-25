package com.vertex.vertex.task_property.value.model.entity;

import com.vertex.vertex.task_property.model.entity.Value;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueDate extends Value{

    private Date date;

}
