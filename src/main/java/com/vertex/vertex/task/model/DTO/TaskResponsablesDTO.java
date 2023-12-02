package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskResponsablesDTO {

    //task id
    private Long taskId;
    private UserTeam userTeam;

}
