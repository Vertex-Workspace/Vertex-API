package com.vertex.vertex.chat.model.DTO;

import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.user.model.DTO.UserChatDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDTO {

    private Long id;
    private List<UserChatDTO> userChatDTOS;
    private List<Message> messages;

}
