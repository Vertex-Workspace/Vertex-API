package com.vertex.vertex.user.controller;

import com.vertex.vertex.user.service.ForgotPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotPassword")
@AllArgsConstructor
@CrossOrigin
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;
    @GetMapping("/{emailTo}")
    public ResponseEntity<Long> sendCodeToEmail(@PathVariable String emailTo){
        try {
            return new ResponseEntity<>(forgotPasswordService.sendCodeToEmail(emailTo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}