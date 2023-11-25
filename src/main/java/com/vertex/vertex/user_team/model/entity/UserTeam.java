package com.vertex.vertex.user_team.model.entity;

import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.permission.model.entity.TaskHour;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Team team;
    @OneToOne
    private User user;

    private Boolean creator;

    @OneToMany(mappedBy = "userTeam")
    private List<TaskHour> permissionUser;

    @OneToMany
    private List<Group> groups;

}
