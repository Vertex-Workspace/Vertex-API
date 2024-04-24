package com.vertex.vertex.chat.model.DTO;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListDTO {
    private Long id;

    private String name;

    private boolean conversationOpened;

    public ChatListDTO(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.conversationOpened = chat.isConversationOpened();
    }
}
