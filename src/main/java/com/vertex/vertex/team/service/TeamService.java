package com.vertex.vertex.team.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public Team findById(Long id) {
        return teamRepository.findById(id).get();
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    //Retornar httpresponse
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

}
