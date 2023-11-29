package com.vertex.vertex.user_team.model.dto;

import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.permission.model.entity.Permission;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
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

}
