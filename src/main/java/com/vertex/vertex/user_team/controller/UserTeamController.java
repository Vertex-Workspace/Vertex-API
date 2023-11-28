package com.vertex.vertex.user_team.controller;

import com.vertex.vertex.user_team.model.DTO.UserTeamDTO;
import com.vertex.vertex.user_team.model.DTO.UserTeamEditionDTO;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import com.vertex.vertex.user_team.service.UserTeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/user-team")
public class UserTeamController {

    private UserTeamService userTeamService;

    @PostMapping
    public ResponseEntity<UserTeam> save(
            @RequestBody UserTeamDTO utc){
        try{
            return new ResponseEntity<>
                    (userTeamService.save(utc),
                            HttpStatus.CREATED);

        } catch (EntityNotFoundException e){
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<UserTeam> save(
            @RequestBody UserTeamEditionDTO utdto){
        try{
            return new ResponseEntity<>
                    (userTeamService.save(utdto),
                            HttpStatus.OK);

        }catch (EntityNotFoundException e){
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserTeam>> findAll(){
        return new ResponseEntity<>
                (userTeamService.findAll(),
                        HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTeam> findById(
            @PathVariable Long id){
        try{
            return new ResponseEntity<>
                    (userTeamService.findById(id),
                            HttpStatus.OK);

        }catch (EntityNotFoundException e){
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable Long id){
        try {
            userTeamService.deleteById(id);
            return new ResponseEntity<>
                    (HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

}
