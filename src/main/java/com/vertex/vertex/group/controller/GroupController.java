package com.vertex.vertex.group.controller;

import com.vertex.vertex.group.model.dto.GroupDTO;
import com.vertex.vertex.group.model.dto.GroupEditionDTO;
import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.group.service.GroupService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/{teamId}")
    public ResponseEntity<Group> save(
            @PathVariable Long teamId, @RequestBody GroupDTO dto) {
        try {
            return new ResponseEntity<>
                    (groupService.save(teamId, dto),
                            HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Group> save(
            @PathVariable Long teamId, @RequestBody GroupEditionDTO dto) {
        try {
            return new ResponseEntity<>
                    (groupService.save(teamId, dto),
                            HttpStatus.OK);

        } catch (EntityExistsException | EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> findById(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>
                    (groupService.findById(id),
                            HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Group>> findById() {
        return new ResponseEntity<>
                (groupService.findAll(),
                        HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {
        try {
            groupService.deleteById(id);
            return new ResponseEntity<>
                    (HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

}
