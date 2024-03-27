package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] image;
    private String kindAsString;

    public ProjectSearchDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.image = project.getFile().getFile();
        this.kindAsString = "Project";
    }

}
