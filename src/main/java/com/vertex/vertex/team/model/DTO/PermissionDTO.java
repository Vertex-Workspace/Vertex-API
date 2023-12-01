package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO extends TeamPatchDefaultDTO {

    private TypePermissions name;

    private Long userId;
}

