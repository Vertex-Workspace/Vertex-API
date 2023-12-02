package com.vertex.vertex.team.relations.user_team.controller;

import com.vertex.vertex.team.model.DTO.TeamHomeDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/user-team")
public class UserTeamController {

    private final UserTeamService userTeamService;

    @GetMapping("/teams/{userId}")
    public ResponseEntity<List<TeamHomeDTO>> findTeamsByUserTeamHome(@PathVariable Long userId) {
        return new ResponseEntity<>(userTeamService.findTeamsByUser(userId), HttpStatus.OK);
    }
}
