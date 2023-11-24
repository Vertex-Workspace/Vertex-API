package com.vertex.vertex.task_property.value.model.entity;

import com.vertex.vertex.property_list.model.entity.PropertyList;
import com.vertex.vertex.task_property.model.entity.TaskProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ValueNumber {
    @Id
    @OneToOne
    private TaskProperty taskProperty;

    private Long number;

}
