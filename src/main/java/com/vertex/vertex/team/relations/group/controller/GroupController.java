package com.vertex.vertex.team.relations.group.controller;

import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin
@RestController
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> findTeamsByUserTeamHome(@PathVariable Long groupId) {
        try {
            return new ResponseEntity<>(groupService.findById(groupId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @DeleteMapping("/team/{teamId}/group/{groupId}/user/{userId}")
    public ResponseEntity<?> deleteUserFromGroup(@PathVariable Long userId, @PathVariable Long teamId, @PathVariable  Long groupId){
        try{
            groupService.deleteUserFromGroup(userId, teamId, groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/team/group")
    public ResponseEntity<?> saveGroup(@RequestBody GroupRegisterDTO groupRegisterDTO) {
        try {
            return new ResponseEntity<>(groupService.saveGroup(groupRegisterDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/team/{teamId}/group/{groupId}")
    public ResponseEntity<?> usersOutOfGroup(@PathVariable Long teamId, @PathVariable Long groupId) {
        try {
            return new ResponseEntity<>(groupService.participantsOutOfGroup(teamId, groupId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/team/group/{id}/addParticipants/{userId}")
    public ResponseEntity<?> addParticipants(@PathVariable Long id, @PathVariable Long userId){
        try{
            groupService.addParticipants(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("team/group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId){
        try{
            groupService.delete(groupId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("group/edit/{teamId}")
    public ResponseEntity<?> editGroup(@RequestBody Group group, @PathVariable Long teamId){
        try {
            return new ResponseEntity<>(groupService.editGroupName(group, teamId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
