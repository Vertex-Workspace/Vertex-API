package com.vertex.vertex.task.relations.note.model.entity;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserTeam creator;

    @ManyToOne
    @ToString.Exclude
    private Project project;

    @OneToMany
    private List<File> files;

    private String color;
    private Integer height;
    private Integer width;
    private Integer posX;
    private Integer posY;

}
