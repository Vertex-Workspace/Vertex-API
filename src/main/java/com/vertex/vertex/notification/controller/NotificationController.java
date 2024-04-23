package com.vertex.vertex.notification.controller;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/user")
public class NotificationController {

    private final NotificationService notificationService;

    //======================================================
    //NOTIFICATIONS
    //======================================================

    @GetMapping("/{userID}/notification")
    public ResponseEntity<?> getNotifications(@PathVariable Long userID){
        try {
            return new ResponseEntity<>(notificationService.getUserNotifications(userID), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/{userID}/notification/delete")
    public ResponseEntity<?> deleteNotifications(@PathVariable Long userID, @RequestBody List<Notification> notifications){
        try {
            notificationService.deleteNotifications(userID, notifications);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{userID}/notification/read")
    public ResponseEntity<?> readNotifications(@PathVariable Long userID, @RequestBody List<Notification> notifications){
        try {
            notificationService.readNotifications(userID, notifications);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }


    @PatchMapping("/{userID}/notification/settings/{settingID}")
    public ResponseEntity<?> notificationSettings(@PathVariable Long userID, @PathVariable Integer settingID){
        try {

            return new ResponseEntity<>(notificationService.changeNotificationSettings(userID, settingID), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

}
