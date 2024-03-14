package com.vertex.vertex.task.relations.note.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserTeam creator;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Project project;

    @OneToMany(cascade = CascadeType.ALL)
    private List<File> files;

    private String color;
    private Integer height;
    private Integer width;
    private Integer posX;
    private Integer posY;

    public Note(Project project,
                UserTeam creator,
                NoteDTO dto) {
        BeanUtils.copyProperties(dto, this);
        this.project = project;
        this.creator = creator;
        this.description = "Sem descrição";

        if (Objects.isNull(project.getNotes())) project.setNotes(List.of(this));
        else project.getNotes().add(this);
    }

}
