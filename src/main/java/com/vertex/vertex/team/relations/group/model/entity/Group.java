package com.vertex.vertex.team.relations.group.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
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
@Table(name = "team_groups")
public class Group{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    private String name;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Team team;


    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<UserTeam> userTeams;



}
