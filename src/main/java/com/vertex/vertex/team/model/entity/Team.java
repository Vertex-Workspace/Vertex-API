package com.vertex.vertex.team.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    private String invitationCode;

    @OneToOne
    private UserTeam creator;

    private String description;

    private LocalDateTime creationDate;

    @Lob
    @Column(name = "image",
            columnDefinition = "BLOB")
    private byte[] image;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Group> groups;

    @OneToOne(cascade = CascadeType.ALL)
    private Chat chat;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTeam> userTeams;



}
