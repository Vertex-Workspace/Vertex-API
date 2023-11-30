package com.vertex.vertex.user_team.repository;

import com.vertex.vertex.user_team.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    List<UserTeam> findAllByTeam_Id(Long teamId);
}
