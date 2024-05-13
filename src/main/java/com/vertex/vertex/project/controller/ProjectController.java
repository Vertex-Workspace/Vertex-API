package com.vertex.vertex.project.controller;

import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin
@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/{teamId}")
    public ResponseEntity<?> save(@RequestBody ProjectCreateDTO project, @PathVariable Long teamId) {
        try {
            return new ResponseEntity<>(projectService.saveWithRelationOfProject(project, teamId), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(projectService.findProjectById(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<?> getProjectName(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(projectService.findById(id).getName(), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> findAllByTeam(
            @PathVariable Long teamId) {
        try {
            return new ResponseEntity<>
                    (projectService.findAllByTeam(teamId),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (e.getMessage(),
                            HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteById(@PathVariable Long id) {
        try {
            projectService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> existsById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>
                    (true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (false,
                            HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exists/{projectId}")
    public ResponseEntity<?> existsByIdAndUserBelongs(
            @PathVariable Long projectId) {
        try {

            return new ResponseEntity<>
                    (projectService.existsByIdAndUserBelongs
                            (projectId),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.UNAUTHORIZED);
        }
    }

    @PatchMapping("/image/{projectId}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long projectId,
            @RequestParam MultipartFile file) {
        try {
            return new ResponseEntity<>
                    (projectService.updateImage(file, projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateProject(@RequestBody ProjectEditDTO projectEditDTO) {
        try {
            return new ResponseEntity<>(projectService.updateProjectCollaborators(projectEditDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll/{projectId}")
    public ResponseEntity<?> returnAllColaborators(@PathVariable Long projectId) {
        try {
            return new ResponseEntity<>(projectService.returnAllCollaborators(projectId), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/query/{query}/{userId}")
    public ResponseEntity<?> findAllByUserAndQuery(
            @PathVariable Long userId, @PathVariable String query) {
        try {
            return new ResponseEntity<>
                    (projectService.findAllByUserAndQuery(userId, query),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{teamId}/{userId}")
    public ResponseEntity<?> getProjectsByCollaborators(@PathVariable Long teamId, @PathVariable Long userId) {
        try {
            return new ResponseEntity<>(projectService.getAllByTeamAndCollaborators(teamId, userId), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
