package com.vertex.vertex.team.relations.group.model.DTO;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEditUserDTO {

    private Long groupId;

    private UserTeam userTeam;

}
