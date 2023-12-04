package com.vertex.vertex.team.relations.user_team.model.DTO;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamAssociateDTO {
    private Long id;
    private Team team;
    private User user;
}
