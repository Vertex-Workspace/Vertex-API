package com.vertex.vertex.user.controller;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.model.DTO.*;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.enums.UserKind;
import com.vertex.vertex.user.model.exception.*;
import com.vertex.vertex.user.relations.personalization.model.entity.LanguageDTO;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) {
        try {
            userDTO.setUserKind(UserKind.DEFAULT);
            return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/password-update")
    public ResponseEntity<Boolean> updatePassword(@RequestBody ChangePasswordDTO dto) {
        try {
            return new ResponseEntity<>(userService.patchUserPassword(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/edit-password")
    public ResponseEntity<?> patchPassword(@RequestBody ChangePasswordForgotDTO dto) {
        try {
            return new ResponseEntity<>(userService.patchUserPasswordForgot(dto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<User> edit(@RequestBody UserEditionDTO userEditionDTO) {
        try {
            return new ResponseEntity<>(userService.edit(userEditionDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        return new ResponseEntity<>
                (userService.findByEmail(email),
                        HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/informations")
    public ResponseEntity<?> findInformationsByID(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.findUserInformations(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/{id}/personalization")
    public ResponseEntity<?> personalization(@PathVariable Long id, @RequestBody Personalization personalization) {
        try {
            return new ResponseEntity<>(this.userService.patchUserPersonalization(id,personalization),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/show-charts")
    public ResponseEntity<?> patchCharts(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.userService.patchUserShowCharts(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }




    @PatchMapping("/{userId}/personalization/changeLanguage")
    public ResponseEntity<?> changeLanguage(@RequestBody LanguageDTO languageDTO, @PathVariable Long userId) {
        try {
            return new ResponseEntity<>(this.userService.changeLanguage(languageDTO,userId),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usersByGroup/{groupId}")
    public ResponseEntity<?> findByGroup(@PathVariable Long groupId) {
        try {
            return new ResponseEntity<>(userService.getUsersByGroup(groupId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("upload/{userId}")
    public void uploadImage(
            @PathVariable Long userId,
            @RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }
        userService.saveImage(file, userId);
    }

    @GetMapping("/query/{query}/{userId}")
    public ResponseEntity<?> findByUserAndQuery(
            @PathVariable Long userId, @PathVariable String query) {
        try {
            return new ResponseEntity<>
                    (userService.findAllByUserAndQuery(userId, query),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }
    @PatchMapping("/first-access/{userId}")
    public ResponseEntity<?> firstAccess(@PathVariable Long userId){
        try {
            userService.setFirstAccessNull(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePasswordPeriodically(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            this.userService.changePasswordPeriodically(changePasswordDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
