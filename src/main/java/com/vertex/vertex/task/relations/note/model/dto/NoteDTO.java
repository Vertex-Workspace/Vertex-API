package com.vertex.vertex.task.relations.note.model.dto;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoteDTO {

    private Long id;
    private String title;
    private String description;
    private List<File> files;
    private Integer height;
    private Integer width;
    private Integer posX;
    private Integer posY;
    private Integer descSize;
    private Integer titleSize;
    private String color;

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.files = note.getFiles();
        this.height = note.getHeight();
        this.width = note.getWidth();
        this.posX = note.getPosX();
        this.posY = note.getPosY();
        this.descSize = note.getDescSize();
        this.titleSize = note.getTitleSize();
        this.color = note.getColor();
    }

}
