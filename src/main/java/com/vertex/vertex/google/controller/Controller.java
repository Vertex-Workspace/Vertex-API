package com.vertex.vertex.google.controller;

import com.vertex.vertex.security.CalendarService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/authorize/google")
@AllArgsConstructor
public class Controller {

    private final CalendarService service;

    @GetMapping
    public void authorize() throws GeneralSecurityException, IOException {

    }

}
