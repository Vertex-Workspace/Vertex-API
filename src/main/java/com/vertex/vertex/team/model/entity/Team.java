package com.vertex.vertex.team.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.project.model.entity.Project;
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

    @OneToMany(mappedBy = "team",
            fetch = FetchType.EAGER)
    private List<Project> projects;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Group> groups;

//    private Image image;

}
