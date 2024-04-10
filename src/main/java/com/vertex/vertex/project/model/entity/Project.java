package com.vertex.vertex.project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
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

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Team team;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties;

    @ManyToOne
    private UserTeam creator;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<Note> notes;

    @ManyToOne
    private Project projectDependency;

    @Lob
    @Column(name = "image",
            columnDefinition = "LONGBLOB")
    @ToString.Exclude
    private byte[] image;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserTeam> collaborators;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Group> groups;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private ProjectReviewENUM projectReviewENUM;


    //create a list of properties if it doesn't exist
    public void addProperty(Property property) {
        if(properties != null){
            properties.add(property);
        }else {
            properties = new ArrayList<>();
            properties.add(property);
        }
    }

    public Project(String name, String description, Team team, UserTeam creator, List<UserTeam>collaborators) {
        this.name = name;
        this.description = description;
        this.team = team;
        this.creator = creator;
        this.collaborators = collaborators;
        this.projectReviewENUM = ProjectReviewENUM.OPTIONAL;
    }
}
