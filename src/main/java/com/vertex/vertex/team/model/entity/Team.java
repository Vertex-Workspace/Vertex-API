package com.vertex.vertex.team.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @OneToOne
    @NotNull
    private UserTeam creator;

    private String description;

    private Date creationDate;

    @OneToMany(mappedBy = "team",
            fetch = FetchType.EAGER)
    private List<Project> projects;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Group> groups;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> userTeams;


}
