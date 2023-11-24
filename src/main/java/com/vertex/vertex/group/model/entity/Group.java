package com.vertex.vertex.group.model.entity;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "team_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    private String name;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Group group;

}
