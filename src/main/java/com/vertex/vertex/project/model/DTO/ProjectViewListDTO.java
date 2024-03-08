package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import lombok.Data;

@Data
public class ProjectViewListDTO {

    private Long id;
    private String name;
    private String description;
    private byte[] image;

    public ProjectViewListDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.image = project.getImage();
    }

}