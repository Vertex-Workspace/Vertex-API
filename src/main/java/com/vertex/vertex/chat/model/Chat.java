package com.vertex.vertex.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.relations.message.Message;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.DTO.UserChatDTO;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    private List<UserTeam> userTeams;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    private boolean conversationOpened;


    public Chat(Team team){
        this.name = team.getName();
        this.userTeams = team.getUserTeams();
        team.getUserTeams().forEach(ut -> ut.getChats().add(this));
    }

}
