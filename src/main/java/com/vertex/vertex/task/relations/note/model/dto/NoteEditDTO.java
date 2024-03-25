package com.vertex.vertex.task.relations.note.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteEditDTO {

    private Long id;
    private String title;
    private String description;
    private String color;
    private Integer height;
    private Integer width;
    private Integer posX;
    private Integer posY;

}
