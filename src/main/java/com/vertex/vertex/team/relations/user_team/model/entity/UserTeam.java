package com.vertex.vertex.team.relations.user_team.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserTeam {

    public UserTeam(User user, Team team){
        this.user = user;
        this.team = team;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Team team;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "userTeam", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Permission> permissionUser;

    @OneToMany
    private List<Group> groups;

    @OneToOne
    @JsonIgnore
    private TaskHour workingOnTaskHour;

}