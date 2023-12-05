package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.DTO.GroupDTO;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Team team) {
        try {
            return new ResponseEntity<>(teamService.save(team), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfoDTO> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.findById(id), HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TeamInfoDTO>> findAll() {
        return new ResponseEntity<>(teamService.findAll(), HttpStatus.OK);

    }

    @PatchMapping("/user")
    public ResponseEntity<?> editUserTeam(@RequestBody UserTeamAssociateDTO userTeam) {
        try {
            return new ResponseEntity<>(teamService.editUserTeam(userTeam), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/group")
    public ResponseEntity<?> editGroup(@RequestBody GroupDTO group) {
        try {
            return new ResponseEntity<>(teamService.editGroup(group), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            teamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
