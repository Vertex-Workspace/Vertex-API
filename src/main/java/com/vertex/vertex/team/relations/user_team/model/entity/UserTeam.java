package com.vertex.vertex.team.relations.user_team.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    @EqualsAndHashCode.Include
    private Team team;

    @ManyToOne
    @EqualsAndHashCode.Include
    private User user;

    @OneToMany(mappedBy = "userTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Permission> permissionUser;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Group> groups;

    @ManyToMany(mappedBy = "userTeams")
    @JsonIgnore
    @ToString.Exclude
    private List<Chat> chats;


    @OneToMany(mappedBy = "userTeam", orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TaskResponsable> taskResponsables;





}
