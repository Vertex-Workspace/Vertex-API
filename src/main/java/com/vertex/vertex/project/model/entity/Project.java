package com.vertex.vertex.project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.enums.CreationOrigin;
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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Property> properties;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private UserTeam creator;

    @OneToMany(mappedBy = "project", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Note> notes;

    @ManyToOne
    @JsonIgnore
    private Project projectDependency;

    @OneToOne(cascade = CascadeType.ALL)
    private File file;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<UserTeam> collaborators = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Group> groups;

    @Enumerated(EnumType.STRING)
    private CreationOrigin creationOrigin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
        this.creationOrigin = CreationOrigin.DEFAULT;
    }
}
