package com.vertex.vertex.team.model.DTO;


import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.DTO.UserChatDTO;
import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserSearchDTO;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


@Data
public class TeamProjectsDTO {
    private Long id;
    private String name;
    private List<ProjectViewListDTO> projects;
    private List<Permission> permissions;

    public TeamProjectsDTO(Team team, UserTeam userTeam){
        this.id = team.getId();
        this.name = team.getName();

        this.projects = team.getProjects()
                .stream()
                .filter(project -> project.getCollaborators().contains(userTeam))
                .map(ProjectViewListDTO::new).toList();

        this.permissions = userTeam.getPermissionUser();
    }
}