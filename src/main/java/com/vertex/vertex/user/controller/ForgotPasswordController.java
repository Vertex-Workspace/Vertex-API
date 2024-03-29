//package com.vertex.vertex.user.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.UnsupportedEncodingException;
//import java.util.Properties;
//
//@RestController
//@RequestMapping("/forgotPassword")
//@AllArgsConstructor
//@CrossOrigin
//public class ForgotPasswordController {
//
//    private final ForgotPasswordService forgotPasswordService;
//    @GetMapping("/{emailTo}")
//    public ResponseEntity<Long> sendCodeToEmail(@PathVariable String emailTo){
//        try {
//            return new ResponseEntity<>(forgotPasswordService.sendCodeToEmail(emailTo), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }
//}
