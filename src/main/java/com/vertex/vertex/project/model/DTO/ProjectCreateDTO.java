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
    private Project projectDependency;
    private List<User> users;
    private List<Group> groups;

    private ProjectReviewENUM projectReviewENUM;

    public ProjectCreateDTO(String name, String description, UserTeam creator, List<User> users) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.users = users;
        this.projectReviewENUM = ProjectReviewENUM.EMPTY;
    }
}

