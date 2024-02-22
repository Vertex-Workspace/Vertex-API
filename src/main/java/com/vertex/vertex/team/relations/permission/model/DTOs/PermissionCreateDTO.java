package com.vertex.vertex.team.relations.permission.model.DTOs;


import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionCreateDTO {

    private TypePermissions name;
    private Long userId;
    private Team team;
}
