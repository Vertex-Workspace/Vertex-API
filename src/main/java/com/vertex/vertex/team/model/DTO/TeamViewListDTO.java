package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamViewListDTO {
    private Long id;
    private String name;
    private UserTeam creator;
    private String description;
    private LocalDateTime creationDate;
    private String image;
}
