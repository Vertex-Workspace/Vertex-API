package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskPropertyDTO {

    private Long id;
    private Value value;

}
