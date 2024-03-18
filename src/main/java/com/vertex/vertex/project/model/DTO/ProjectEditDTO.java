package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ProjectEditDTO {

    private Long id;
    private String name;
    private String description;
    private List<UserTeam> listOfResponsibles;

}
