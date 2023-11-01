package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private TeamService teamService;

    @PostMapping
    public Team save(
            @RequestBody Team team) {
        return teamService.save(team);
    }

    @GetMapping("/{id}")
    public Optional<Team> findById(
            @PathVariable Long id) {
        return teamService.findById(id);
    }

    @GetMapping
    public List<Team> findAll() {
        return teamService.findAll();
    }

    @PutMapping
    public Team update(
            @RequestBody Team team) {
        return teamService.save(team);
    }

    @DeleteMapping
    public void delete(
            @RequestParam Long id) {
//        return
        teamService.deleteById(id);
        //retorna httpresponse
    }

}
