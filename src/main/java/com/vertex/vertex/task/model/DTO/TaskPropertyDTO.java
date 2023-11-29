package com.vertex.vertex.task.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.entity.TaskPropertyDeserializer;
import com.vertex.vertex.task.value.model.entity.Value;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskPropertyDTO {

    private Long id;
    private Value value;
}
