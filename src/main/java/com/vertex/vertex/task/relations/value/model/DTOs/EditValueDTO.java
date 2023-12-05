package com.vertex.vertex.task.relations.value.model.DTOs;

import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditValueDTO {

    //this is the task id
    private Long id;
    private Value value;

}
