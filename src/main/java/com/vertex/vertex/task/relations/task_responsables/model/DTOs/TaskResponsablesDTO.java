package com.vertex.vertex.task.relations.task_responsables.model.DTOs;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponsablesDTO {

    private Long id;
    private Task task;
    private UserTeam userTeam;

}
