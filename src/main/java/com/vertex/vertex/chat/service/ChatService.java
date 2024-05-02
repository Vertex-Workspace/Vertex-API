package com.vertex.vertex.chat.service;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.model.DTO.ChatListDTO;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.relations.message.MessageRepository;
import com.vertex.vertex.chat.repository.ChatRepository;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserTeamService userTeamService;
    private final TaskService taskService;


    public List<ChatListDTO> findAllByUser(Long userID) {
        return userTeamService.findAllUserTeamByUserId(userID)
                .stream()
                .flatMap(userTeam -> userTeam.getChats().stream())
                .map(ChatListDTO::new)
                .toList();
    }

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

    public Chat patchMessages(Long idChat, User user, Message message) {
        Chat chat = chatRepository.findById(idChat).get();
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

    public Task createChatOfTask(Long idTask){
        Task task = taskService.findById(idTask);
        List<UserTeam> userTeamsList = new ArrayList<>();
        task.getTaskResponsables().forEach(taskResponsable ->{
            userTeamsList.add(taskResponsable.getUserTeam());
        });

        Chat chat = new Chat();
        chat.setName(task.getName());
        chat.setUserTeams(userTeamsList);
        chat.setMessages(new ArrayList<>());
        task.setChatCreated(true);
        task.setChat(chat);
        save(chat);

        return task;
    }

}
