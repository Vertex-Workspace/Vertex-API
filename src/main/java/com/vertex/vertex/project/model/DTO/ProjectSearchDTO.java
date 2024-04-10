package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] image;
    private String kindAsString;
    private List<NoteDTO> notes;

    public ProjectSearchDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();

        if (!Objects.isNull(project.getImage())) this.image = project.getImage();

        this.kindAsString = "Projeto";
    }

}
