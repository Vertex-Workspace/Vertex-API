package com.vertex.vertex.chat.service;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.relations.message.MessageRepository;
import com.vertex.vertex.chat.repository.ChatRepository;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat create(Chat chat) {
        System.out.println("CHAT SERVICE " + chat);
        return chatRepository.save(chat);
    }

    public List<Message> findMessagesByChatId(Long idChat){
        return messageRepository.findAllByChat_Id(idChat);
    }

    public Chat patchUserTeams(Long idChat, UserTeamAssociateDTO userTeamAssociateDTO) {

        Chat chat = chatRepository.findById(idChat).get();
//        System.out.println("UserTeam Associate DTO"+userTeamAssociateDTO);
        UserTeam userTeam = userTeamRepository.findByTeam_IdAndUser_Id(userTeamAssociateDTO.getTeam().getId(), userTeamAssociateDTO.getUser().getId());

//        System.out.println("FINDALL "+ userTeamRepository.findAll());

//        System.out.println("userTeam"+userTeam);
        chat.getUserTeams().add(userTeam);
//        System.out.println("LISTA USERTEAMS-CHAT"+chat.getUserTeams());

//        System.out.println("CHAT SERVICE 2" + chat);
        return chatRepository.save(chat);
    }

    public Chat patchMessages(Long idChat,Long idUser, Message message) {
        Chat chat = chatRepository.findById(idChat).get();
        User user = userRepository.findById(idUser).get();
        Message message1 = new Message();
        message1.setChat(chat);
        message1.setUser(user);
        message1.setTime(message.getTime());
        message1.setVisualized(message.isVisualized());
        message1.setContentMessage(message.getContentMessage());
        System.out.println("CHAT: " + chat);
        System.out.println("MESSAGE: " + message);
        chat.getMessages().add(message1);
        System.out.println("CHAT - MESSAGES: " + chat.getMessages());
        return chatRepository.save(chat);
    }


}
