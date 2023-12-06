package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskModeViewDTO {

    private Long id;
    private String name;
    private List<TaskResponsable> responsables;
    private List<PropertyList> propertyLists;
    private List<Value> values;
}
