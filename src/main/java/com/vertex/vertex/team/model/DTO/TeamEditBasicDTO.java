package com.vertex.vertex.team.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamEditBasicDTO {
    private Long id;
    private String name;
    private String description;
}
