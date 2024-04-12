package com.vertex.vertex.team.relations.user_team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin
@RestController
@AllArgsConstructor
public class UserTeamController {

    private final UserTeamService userTeamService;

    @GetMapping("/user-team/teams/{userId}")
    public ResponseEntity<List<TeamViewListDTO>> findTeamsByUserTeamHome(@PathVariable Long userId) {
        return new ResponseEntity<>(userTeamService.findTeamsByUser(userId), HttpStatus.OK);
    }


    @GetMapping("/team/permission/{userId}/{teamId}")
    public ResponseEntity<?> getAllPermissions(@PathVariable Long userId, @PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(userTeamService.getAllPermissionOfAUserTeam(userId, teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/team/user")
    public ResponseEntity<?> saveUserTeam(@RequestBody UserTeamAssociateDTO userTeam) {
        try {
            return new ResponseEntity<>(userTeamService.saveNewUserTeam(userTeam), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/team/user-team/{teamId}/{userId}")
    public ResponseEntity<?> deleteUserTeam(@PathVariable Long userId, @PathVariable Long teamId){
        try{
            return new ResponseEntity<>(userTeamService.delete(teamId, userId), HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/team/userIsOnTeam/{idUser}/{idTeam}")
    public ResponseEntity<?> userIsOnTeam(@PathVariable Long idUser, @PathVariable Long idTeam){
        try {
            userTeamService.findUserTeamByComposeId(idTeam, idUser);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }


}
