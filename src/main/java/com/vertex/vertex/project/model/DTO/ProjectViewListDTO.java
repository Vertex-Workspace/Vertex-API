package com.vertex.vertex.project.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.user.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@NoArgsConstructor
public class ProjectViewListDTO {

    private Long id;
    private String name;
    private String description;
    private File file;
    private Boolean isCreator;

    public ProjectViewListDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.file = project.getFile();
        this.isCreator = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(project.getCreator().getUser().getId());

    }

}
