package com.vertex.vertex.team.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //Services
    private final UserService userService;

    public Team save(Team team) {
        try {
            //Create a new row at table User_Team based on the user that has been created the team
            UserTeam userTeam = new UserTeam(userService.findById(team.getCreator().getId()), team);
            team.setUserTeams(List.of(userTeam));
            team.setCreator(userTeam);

            //After the Romas explanation about Date
//            team.setCreationDate();

            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Team update(Team team) {
        return teamRepository.save(team);
    }

    public Team updateUserTeam(UserTeam userTeam) {
        try {
            return teamRepository.save(findUserAndTeam(userTeam));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Team findUserAndTeam(UserTeam userTeam){
        try {
            User user = userService.findById(userTeam.getUser().getId());
            Team team = findById(userTeam.getTeam().getId());
            team.getUserTeams().add(new UserTeam(user, team))
            return team;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Team findById(Long id) {
        if (teamRepository.existsById(id)) {
            return teamRepository.findById(id).get();
        }
        throw new TeamNotFoundException(id);
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public void deleteById(Long id) {
        try {
            Team team = findById(id);
            teamRepository.delete(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

}
