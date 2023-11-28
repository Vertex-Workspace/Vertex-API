package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> save(
            @RequestBody Team team) {
        try {
            return new ResponseEntity<>
                    (teamService.save(team),
                            HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> findById(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>
                    (teamService.findById(id),
                            HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Team>> findAll() {
            return new ResponseEntity<>
                    (teamService.findAll(),
                            HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<Team> update(
            @RequestBody Team team) {
        try {
            return new ResponseEntity<>
                    (teamService.update(team),
                            HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        try {
            teamService.deleteById(id);
            return new ResponseEntity<>
                    (HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

}
