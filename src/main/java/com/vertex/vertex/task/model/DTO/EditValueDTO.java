package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditValueDTO {

    private Long id;
    private Value value;

}
