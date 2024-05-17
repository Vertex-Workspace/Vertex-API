package com.vertex.vertex.google.controller;

import com.vertex.vertex.google.service.CalendarService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/google/calendar")
@AllArgsConstructor
@CrossOrigin
public class CalendarController {

    private final CalendarService service;

    @PostMapping("/{userId}/{teamId}")
    public ResponseEntity<?> create(
            @PathVariable Long userId,
            @PathVariable Long teamId,
            @RequestBody ProjectCreateDTO dto,
            HttpServletResponse response
    ) {
        try {
            return new ResponseEntity<>
                    (service.createCalendarProject(teamId, dto, userId, response),
                            HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{userId}/{projectId}")
    public ResponseEntity<?> getAndUpdate(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            HttpServletResponse response
    ) {
        try {
            return new ResponseEntity<>
                    (service.findByIdAndGetNewEvents(projectId, userId, response),
                            HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> create(
            @PathVariable Long userId,
            @RequestBody Long projectId,
            HttpServletResponse response
    ) {
        try {
            return new ResponseEntity<>
                    (service.create(response, userId, projectId),
                            HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }




}
