package com.vertex.vertex.chat.service;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.chat.repository.ChatRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserTeamService userTeamService;
    private final UserTeamRepository userTeamRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat create(Team team) {

        Chat chat = new Chat();

//        chat.setTeam(team);
//        List<User> users = new ArrayList<>();
        return chat;

    }


}
