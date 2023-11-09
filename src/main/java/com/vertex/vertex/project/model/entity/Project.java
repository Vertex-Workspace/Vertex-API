package com.vertex.vertex.project.model.entity;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.team.model.entity.Team;
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
}
