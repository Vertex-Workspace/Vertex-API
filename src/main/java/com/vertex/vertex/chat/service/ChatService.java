package com.vertex.vertex.chat.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.relations.message.MessageRepository;
import com.vertex.vertex.chat.repository.ChatRepository;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserTeamService userTeamService;
    private final UserRepository userRepository;


    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat create(Chat chat) {
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


    public Message saveFile(Long chatId, MultipartFile file, String user) throws IOException {

        Chat chat = chatRepository.findById(chatId).get();

        File file1 = new File(file);

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setTime(LocalDateTime.now());
        message.setVisualized(false);

        message.setFile(file1);

//            System.out.println(message);
        chat.getMessages().add(message);

        System.out.println("MESSAGE"+message);
        chatRepository.save(chat);
        return message;


    }
}
