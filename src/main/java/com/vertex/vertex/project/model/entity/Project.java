package com.vertex.vertex.project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

//    private String image;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Team team;

    @OneToMany(mappedBy = "project")
    private List<Property> properties;

    @ManyToOne
    private UserTeam creator;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToOne
    private Project projectDependency;

}
