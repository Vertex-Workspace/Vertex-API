package com.vertex.vertex.team.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;


    //Services
    private final UserService userService;
    public Team save(Team team) {
        //Create a new row at table User_Team based on the user that has been created the team
        UserTeam userTeam = new UserTeam(userService.findById(team.getCreator().getId()), team);
        team.setUserTeams(List.of(userTeam));
        team.setCreator(userTeam);

        return teamRepository.save(team);
    }

    public Team update(Team team) {
        if (teamRepository.existsById(team.getId())) {
            return teamRepository.save(team);
        }
        throw new EntityNotFoundException();
    }

    public Team findById(Long id) {
        if (teamRepository.existsById(id)) {
            return teamRepository.findById(id).get();
        }
        throw new EntityNotFoundException();
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public void deleteById(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

}
