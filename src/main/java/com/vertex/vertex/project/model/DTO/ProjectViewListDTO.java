package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.entity.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectViewListDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] file;

    public ProjectViewListDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.file = project.getImage();
    }

}
