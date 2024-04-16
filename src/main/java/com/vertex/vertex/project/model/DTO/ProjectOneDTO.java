package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.note.model.dto.NoteDTO;
import com.vertex.vertex.task.relations.note.model.entity.Note;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOneDTO {
    private Long id;
    private String name;
    private String description;
    private Long idTeam;
    private Project projectDependency;
    private List<Property> properties;
    private UserTeam creator;
    private File file;
    private List<Task> tasks;
    private List<NoteDTO> notes;
    private ProjectReviewENUM projectReviewENUM;

    public ProjectOneDTO(Project project) {
        BeanUtils.copyProperties(project, this);
        this.notes = project.getNotes()
                .stream()
                .map(NoteDTO::new)
                .toList();
    }
}
