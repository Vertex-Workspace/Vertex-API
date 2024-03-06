package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String description;
    private LocalDateTime creationDate;
    private Chat chat;

}
