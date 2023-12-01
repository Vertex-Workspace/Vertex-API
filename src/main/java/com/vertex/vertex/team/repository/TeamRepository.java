package com.vertex.vertex.team.repository;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository
        extends JpaRepository<Team, Long> {
}
