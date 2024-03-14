package com.vertex.vertex.project.controller;

import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final FileService fileService;

//    @PostMapping("/{teamId}")
//    public ResponseEntity<?> save(@RequestBody ProjectCreateDTO project, @PathVariable Long teamId){
//        try {
//            System.out.println(project);
//            System.out.println(teamId);
//            projectService.save(project, teamId);
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        }catch(Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }

    @PostMapping("/{teamId}")
    public void save(@RequestBody ProjectCreateDTO project, @PathVariable Long teamId){
        projectService.save(project, teamId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(projectService.findProjectById(id), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }
    }

    @PutMapping
    public ResponseEntity<Project> update(@RequestBody Project project){
        try {
            return new ResponseEntity<>(projectService.save(project), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> existsById(@PathVariable Long id) {
        if (projectService.existsById(id)) {
            return new ResponseEntity<>
                    (true, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>
                    (false,
                            HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exists/{projectId}/{userId}")
    public ResponseEntity<?> existsByIdAndUserBelongs(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        return new ResponseEntity<>
                (projectService.existsByIdAndUserBelongs
                        (projectId, userId),
                        HttpStatus.OK);
    }

    @PatchMapping("/image/{projectId}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long projectId,
            @RequestParam MultipartFile file) {
        try {
            projectService.updateImage(file, projectId);
            return new ResponseEntity<>
                    (HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{teamId}/{userId}")
    public ResponseEntity<?> getProjectsByCollaborators(@PathVariable Long teamId, @PathVariable Long userId){
        try {
            return new ResponseEntity<>(projectService.getAllByTeamAndCollaborators(teamId, userId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
