package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoDTO extends TeamViewListDTO{
    private List<User> users;
    private List<Project> projects;
    private List<Group> groups;

}
