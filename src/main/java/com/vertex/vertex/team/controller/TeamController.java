package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupEditUserDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TeamViewListDTO team) {
        try {
            return new ResponseEntity<>(teamService.save(team), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody TeamViewListDTO team) {
        try {
            return new ResponseEntity<>(teamService.save(team), HttpStatus.OK);
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

    @GetMapping("/invitation/{id}")
    public ResponseEntity<?> findInvitationCodeById(@PathVariable Long id) {

        try {
            return new ResponseEntity<>(teamService.findInvitationCodeById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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

    @GetMapping("/{idTeam}/groups")
    public ResponseEntity<?> findGroupsByTeamId(@PathVariable Long idTeam) {
        try {
            return new ResponseEntity<>(teamService.findGroupsByTeamId(idTeam), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //EDIT CASCADE TYPE ALL OBJECTS
    //ADD USER IN THE TEAM
    @PatchMapping("/user")
    public ResponseEntity<?> editUserTeam(@RequestBody UserTeamAssociateDTO userTeam) {
        try {
            return new ResponseEntity<>(teamService.editUserTeam(userTeam), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/group")
    public ResponseEntity<?> editGroup(@RequestBody GroupRegisterDTO groupRegisterDTO) {
        try {
            return new ResponseEntity<>(teamService.editGroup(groupRegisterDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/userIsOnTeam/{idUser}/{idTeam}")
    public boolean userIsOnTeam(@PathVariable Long idUser, @PathVariable Long idTeam){
        return teamService.userIsOnTeam(idUser,idTeam);
    }

    //

    @GetMapping("/exists/{teamId}/{userId}")
    public ResponseEntity<?> existsByIdAndUserBelongs(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        return new ResponseEntity<>
                (teamService.existsByIdAndUserBelongs(teamId, userId),
                        HttpStatus.OK);
    }


    @PatchMapping("/group/user")
    public ResponseEntity<?> editUserIntoGroup(@RequestBody GroupEditUserDTO groupEditUserDTO) {
        try {
            return new ResponseEntity<>(teamService.editUserIntoGroup(groupEditUserDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/image/{teamId}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long teamId,
            @RequestParam MultipartFile file) {
        try {
            teamService.updateImage(file, teamId);
            return new ResponseEntity<>
                    (HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

}
