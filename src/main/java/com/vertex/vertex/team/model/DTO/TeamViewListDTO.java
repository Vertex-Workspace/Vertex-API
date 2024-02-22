package com.vertex.vertex.team.model.DTO;

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
}
