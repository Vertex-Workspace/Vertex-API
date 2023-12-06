package com.vertex.vertex.team.relations.user_team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
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

    private UserTeamService userTeamService;

    @GetMapping("/teams/{userId}")
    public ResponseEntity<List<TeamViewListDTO>> findTeamsByUserTeamHome(@PathVariable Long userId) {
        return new ResponseEntity<>(userTeamService.findTeamsByUser(userId), HttpStatus.OK);
    }

}
