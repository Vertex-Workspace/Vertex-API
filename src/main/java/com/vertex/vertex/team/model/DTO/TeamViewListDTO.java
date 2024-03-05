package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamViewListDTO {
    private Long id;
    private String name;
    private User creator;
    private byte[] image;
    private String description;
    private LocalDateTime creationDate;
    private boolean defaultTeam;
    private Chat chat;

    public TeamViewListDTO(String name, User creator, byte[] image, String description, LocalDateTime creationDate, boolean defaultTeam) {
        this.name = name;
        this.creator = creator;
        this.image = image;
        this.description = description;
        this.creationDate = creationDate;
        this.defaultTeam = true;
    }
}
