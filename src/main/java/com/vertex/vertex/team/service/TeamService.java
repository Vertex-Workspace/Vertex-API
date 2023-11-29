package com.vertex.vertex.team.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
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
<<<<<<< HEAD
        } else{
=======
        } else {
>>>>>>> 26523a993084336bb4fcc3889d3cc3e2fbd27026
            throw new EntityNotFoundException();
        }
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

}
