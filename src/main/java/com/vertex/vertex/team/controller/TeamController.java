package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupEditUserDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.permission.model.DTOs.PermissionCreateDTO;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/permission")
    public ResponseEntity<?> giveAPermission(@RequestBody PermissionCreateDTO permissionCreateDTO){

        try{
            return new ResponseEntity<>(permissionService.save(permissionCreateDTO), HttpStatus.OK);
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


}
