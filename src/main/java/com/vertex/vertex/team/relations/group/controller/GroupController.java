package com.vertex.vertex.team.relations.group.controller;

import com.vertex.vertex.team.relations.group.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<?> findTeamsByUserTeamHome(@PathVariable Long groupId) {
        try {
            return new ResponseEntity<>(groupService.findById(groupId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
