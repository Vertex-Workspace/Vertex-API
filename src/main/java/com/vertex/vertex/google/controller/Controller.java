package com.vertex.vertex.google.controller;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.vertex.vertex.security.CalendarService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/authorize/google")
@AllArgsConstructor
public class Controller {

    private final CalendarService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> authorize(
            @PathVariable Long userId, HttpServletResponse response)
            throws GeneralSecurityException, IOException {
        try {
            service.getCredentials(response, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
