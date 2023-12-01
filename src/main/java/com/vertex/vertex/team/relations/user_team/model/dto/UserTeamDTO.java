package com.vertex.vertex.team.relations.user_team.model.dto;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamDTO {

    private Long teamId;
    private Long userId;
    private Boolean creator;
    private List<Permission> permissionUser;
    private List<Group> groups;

    //new feature task_hours
    private Long workingOnTaskHour;

}
