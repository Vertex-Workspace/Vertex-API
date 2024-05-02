package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ProjectEditDTO {

    private Long id;
    private String name;
    private String description;
    private Project projectDependency;
    private ProjectReviewENUM projectReviewENUM;
    private List<User> users;
    private List<Group> groups;

}
