package com.vertex.vertex.chat.repository;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {



}
