package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.team.model.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamSearchDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] image;
    private String kindAsString;

    public TeamSearchDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.image = team.getImage();
        this.kindAsString = "Team";
    }
}
