package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.team.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
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
}
