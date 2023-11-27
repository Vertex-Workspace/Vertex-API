package com.vertex.vertex.user_team.controller;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserEditionDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import com.vertex.vertex.user_team.service.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/user-team")
public class UserTeamController {

    private UserTeamService userTeamService;

    @PostMapping
    public ResponseEntity<UserTeam> save(@RequestBody UserDTO userDTO){
        try{
            return new ResponseEntity<>(userTeamService.save(userDTO),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<UserTeam> save(@RequestBody UserEditionDTO userEditionDTO){
        try{
            return new ResponseEntity<>(userTeamService.save(userEditionDTO),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<UserTeam>> findAll(){
        return new ResponseEntity<>(userTeamService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTeam> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(userTeamService.findById(id),HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userTeamService.deleteById(id);
    }

}
