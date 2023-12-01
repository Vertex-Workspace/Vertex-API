package com.vertex.vertex.team.relations.user_team.repository;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    List<UserTeam> findAllByTeam_Id(Long teamId);

    UserTeam findByTeam_IdAndUser_Id(Long teamId, Long userId);

    List<UserTeam> findAllByUser_Id(Long userID);
}
