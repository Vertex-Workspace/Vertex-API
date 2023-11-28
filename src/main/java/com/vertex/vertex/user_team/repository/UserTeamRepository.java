package com.vertex.vertex.user_team.repository;

import com.vertex.vertex.user_team.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

}
