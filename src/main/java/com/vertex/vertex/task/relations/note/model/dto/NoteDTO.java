package com.vertex.vertex.task.relations.note.model.dto;

import com.vertex.vertex.file.File;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoteDTO {

    private String title;
    private String description;
    private Integer height;
    private Integer width;
    private Integer posX;
    private Integer posY;

}
