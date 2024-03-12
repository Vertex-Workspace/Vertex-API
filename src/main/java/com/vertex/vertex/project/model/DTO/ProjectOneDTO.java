package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOneDTO {
    private Long id;
    private String name;
    private String description;
    private Long idTeam;
    private List<Property> properties;
    private UserTeam creator;
    private List<Task> tasks;
    private List<Note> notes;
}
