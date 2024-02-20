package com.vertex.vertex.chat.repository;

import com.vertex.vertex.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

//    List<Chat> findAllByUserTeam_id(Long id);
}
