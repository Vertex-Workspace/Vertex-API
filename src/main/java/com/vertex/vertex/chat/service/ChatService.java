package com.vertex.vertex.chat.service;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.relations.message.MessageRepository;
import com.vertex.vertex.chat.repository.ChatRepository;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserTeamService userTeamService;
    private final UserRepository userRepository;



    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    public List<Message> findMessagesByChatId(Long idChat) {
        return messageRepository.findAllByChat_Id(idChat);
    }

    public Chat patchUserTeams(Long idChat, UserTeamAssociateDTO userTeamAssociateDTO) {
        Chat chat = chatRepository.findById(idChat).get();
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(userTeamAssociateDTO.getTeam().getId(), userTeamAssociateDTO.getUser().getId());
        chat.getUserTeams().add(userTeam);
        return chatRepository.save(chat);
    }

    public Chat patchMessages(Long idChat, Long idUser, Message message) {
        Chat chat = chatRepository.findById(idChat).get();
        User user = userRepository.findById(idUser).get();


        Message message1 = new Message();
        message1.setChat(chat);
        message1.setUser(user.getFirstName());
        message1.setTime(LocalDateTime.now());
        message1.setVisualized(message.isVisualized());
        message1.setContentMessage(message.getContentMessage());
        message1.setFile(message.getFile());

        chat.getMessages().add(message1);
        return chatRepository.save(chat);
    }


    public Chat saveFile(Long chatId, MultipartFile file, String user) {

        Chat chat = chatRepository.findById(chatId).get();

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setTime(LocalDateTime.now());
        message.setVisualized(false);

        try {
            message.setFile(new File(file));
            chat.getMessages().add(message);
            return chatRepository.save(chat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Chat saveNewTeamChat(Team team){
        Chat chat = new Chat();
        chat.setUserTeams(team.getUserTeams());
        chat.setName(team.getName());

        try {
            Chat chatSaved = save(chat);
            //Add chat for user teams
            for (UserTeam userTeam : team.getUserTeams()) {
                userTeam.getChats().add(chatSaved);
                userTeamService.save(userTeam);
            }

            return chatSaved;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar um chat!");
        }
    }
}
