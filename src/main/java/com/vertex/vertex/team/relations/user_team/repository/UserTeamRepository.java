package com.vertex.vertex.team.relations.user_team.repository;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    List<UserTeam> findAllByTeam_Id(Long teamId);

    Optional<UserTeam> findByTeam_IdAndUser_Id(Long teamId, Long userId);

    List<UserTeam> findAllByUser_Id(Long userId);

    List<UserTeam> findAllByUser_IdAndTeam_NameContainingIgnoreCase(Long userId, String name);


}
