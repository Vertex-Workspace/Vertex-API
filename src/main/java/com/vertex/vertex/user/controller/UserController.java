package com.vertex.vertex.user.controller;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserEditionDTO;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.*;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.service.PersonalizationService;
import com.vertex.vertex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private PersonalizationService personalizationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) {
        try {
            return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);
        } catch (EmailAlreadyExistsException
                | InvalidEmailException
                | InvalidPasswordException
                | UnsafePasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Preencha todos os campos!", HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<User> edit(@RequestBody UserEditionDTO userEditionDTO) {
        try {
//            System.out.println(userEditionDTO);
            return new ResponseEntity<>(userService.edit(userEditionDTO), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        return new ResponseEntity<>
                (userService.findByEmail(email),
                        HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.FOUND);
        } catch (NoSuchElementException e) {
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

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody UserLoginDTO dto) {

        try {
            return new ResponseEntity<>
                    (userService.authenticate(dto),
                            HttpStatus.ACCEPTED);

        } catch (UserNotFoundException | IncorrectPasswordException e) {
            return new ResponseEntity<>
                    ("E-mail ou senha inválidos!",
                            HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>
                    ("Outro erro!",
                            HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}/personalization")
    public ResponseEntity<?> patchControllerUser(@PathVariable Long id, @RequestBody Personalization personalization) {
        try {
            return new ResponseEntity<>(this.userService.patchUserPersonalization(id,personalization),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

}
