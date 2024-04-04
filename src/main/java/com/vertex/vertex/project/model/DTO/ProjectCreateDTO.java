package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectCreateDTO {

    private String name;
    private String description;
    private byte[] image;
    private UserTeam creator;
    private Team team;
    private Project projectDependency;
//    private File file;
    private List<User> users;
    private List<Group> groups;

    private ProjectReviewENUM projectReviewENUM;

}

