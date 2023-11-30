package com.vertex.vertex.user_team.controller;

import com.vertex.vertex.user_team.model.dto.UserTeamDTO;
import com.vertex.vertex.user_team.model.dto.UserTeamEditionDTO;
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

    @GetMapping("{/get-team-users/{teamId}")
    public ResponseEntity<List<UserTeam>> findAllByTeam(@PathVariable Long teamId){
        return new ResponseEntity<>
                (userTeamService.findAllByTeamId(teamId),
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

}
