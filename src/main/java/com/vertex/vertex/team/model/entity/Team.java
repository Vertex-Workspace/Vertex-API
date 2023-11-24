package com.vertex.vertex.team.model.entity;

import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;
    private String description;
    private Date creationDate;

    @OneToMany(mappedBy = "team")
    private List<Project> projects;

    @OneToMany(mappedBy = "team")
    private List<Group> groups;

//    private Image image;

}
