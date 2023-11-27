package com.vertex.vertex.project.model.entity;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property_list.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String image;
    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "project")
    private List<Property> properties;

    @OneToOne
    private UserTeam creator;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToOne
    private Project projectDependency;

}
