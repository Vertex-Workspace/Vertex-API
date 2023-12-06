package com.vertex.vertex.team.relations.group.model.DTO;

import com.vertex.vertex.team.model.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRegisterDTO {
    private Long id;

    private String name;

    private Team team;

}
