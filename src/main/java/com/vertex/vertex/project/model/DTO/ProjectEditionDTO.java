package com.vertex.vertex.project.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectEditionDTO {

    private Long id;
    private String name;
    private String description;
    private String image;
}
