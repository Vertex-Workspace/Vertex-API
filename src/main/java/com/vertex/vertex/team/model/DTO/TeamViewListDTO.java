package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeamViewListDTO {

    @EqualsAndHashCode.Include
    private Long id;
    private byte[] image;
    private String name;
    private User creator;
    private Boolean isCreator;
    private String description;
    private LocalDateTime creationDate;
    private boolean defaultTeam;
    private List<ProjectViewListDTO> projects;

    public TeamViewListDTO(String name, User creator, byte[] image, String description, LocalDateTime creationDate, boolean defaultTeam) {
        this.name = name;
        this.creator = creator;
        this.image = image;
        this.description = description;
        this.creationDate = creationDate;
        this.defaultTeam = defaultTeam;
        this.isCreator = true;
    }

    public TeamViewListDTO(String name, User creator, byte[] image, String description, boolean defaultTeam) {
        this.name = name;
        this.creator = creator;
        this.image = image;
        this.description = description;
        this.defaultTeam = defaultTeam;
        this.isCreator = true;
    }
}
