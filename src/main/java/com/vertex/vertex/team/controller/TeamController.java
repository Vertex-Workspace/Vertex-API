package com.vertex.vertex.team.controller;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamProjectsDTO;
import com.vertex.vertex.team.model.DTO.TeamSearchDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TeamViewListDTO team) {
        try {
            return new ResponseEntity<>(teamService.save(team), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody TeamViewListDTO team) {
        try {
            return new ResponseEntity<>(teamService.save(team), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfoDTO> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.findById(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/screen/{id}")
    public ResponseEntity<TeamProjectsDTO> teamScreenInformations(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.getTeamScreenInformation(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/name/{id}")
    public ResponseEntity<?> getTeamName(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.getTeamName(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/invitation/{id}")
    public ResponseEntity<?> findInvitationCodeById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.findInvitationCodeById(id), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/invitation-page-info/{id}")
    public ResponseEntity<TeamSearchDTO> findTeamInvitationPage(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(teamService.findTeamInvitationPage(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            teamService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idTeam}/groups")
    public ResponseEntity<?> findGroupsByTeamId(@PathVariable Long idTeam) {
        try {
            return new ResponseEntity<>(teamService.findGroupsByTeamId(idTeam), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/usersByTeam/{teamId}")
    public ResponseEntity<?> findUsersAndGroupsByTeam(@PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(teamService.getUsersByTeamAndGroup(teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{teamId}")
    public ResponseEntity<?> findByTeam(@PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(teamService.getUsersByTeam(teamId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/image/{teamId}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long teamId,
            @RequestParam MultipartFile file) {
        try {
            return new ResponseEntity<>(teamService.updateImage(file, teamId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> findAllTasksByTeam(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    teamService.getAllTasksByTeam(id),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Equipe não encontrada!",
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/query/{query}/{userId}")
    public ResponseEntity<?> findAllByUserAndQuery(
            @PathVariable Long userId, @PathVariable String query) {
        try {
            return new ResponseEntity<>
                    (teamService.findAllByUserAndQuery(userId, query),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/exc")
    public ResponseEntity<?> throwException() {
        throw new RuntimeException("Erro");
    }

}
