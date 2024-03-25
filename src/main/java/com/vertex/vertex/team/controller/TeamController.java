package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.AddUsersDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupEditUserDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final GroupService groupService;
    private final PermissionService permissionService;

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
            teamService.save(team);
            return new ResponseEntity<>(HttpStatus.OK);
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
            System.out.println(e.getMessage());
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

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId){
        try{
            groupService.delete(groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/usersByTeam/{teamId}")
    public ResponseEntity<?> findByTeam(@PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(teamService.getUsersByTeam(teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{teamId}/group/{groupId}/user/{userId}")
    public ResponseEntity<?> deleteUserFromGroup(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable  Long groupId){
        try{
            groupService.deleteUserFromGroup(userId, teamId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/permission/{permissionId}/{userId}/{teamId}")
    public ResponseEntity<?> giveAPermission(@PathVariable Long permissionId, @PathVariable Long userId, @PathVariable Long teamId){
        try{
            permissionService.changeEnabled(permissionId, userId, teamId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/permission/{userId}/{teamId}")
    public ResponseEntity<?> getAllPermissions(@PathVariable Long userId, @PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(permissionService.getAllPermissionOfAUserTeam(userId, teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/hasPermission/{projectId}/{userId}")
    public ResponseEntity<?> hasPermission(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            return new ResponseEntity<>(permissionService.hasPermission(projectId, userId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{teamId}/group/{groupId}")
    public ResponseEntity<?> usersOutOfGroup(@PathVariable Long teamId, @PathVariable Long groupId) {
        try {
            return new ResponseEntity<>(groupService.participantsOutOfGroup(teamId, groupId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/group/{id}/addParticipants")
    public ResponseEntity<?> addParticipants(@PathVariable Long id, @RequestBody AddUsersDTO addUsersDTO){
        try{
            groupService.addParticipants(id, addUsersDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/user-team/{teamId}/{userId}")
    public ResponseEntity<?> deleteUserFromGroup(@PathVariable Long userId, @PathVariable Long teamId){
        try{
            teamService.deleteUserTeam(teamId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> findAllByTeam(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    teamService.getAllTasksByTeam(id),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Equipe não encontrada!",
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{teamId}/creator")
    public ResponseEntity<?> creatorOfTeam(@PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(teamService.teamCreatorId(teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
