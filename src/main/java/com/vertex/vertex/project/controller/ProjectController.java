package com.vertex.vertex.project.controller;

import com.vertex.vertex.project.model.DTO.ProjectDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditionDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> save(@RequestBody ProjectDTO projectDTO){
        try {
            return new ResponseEntity<>(projectService.save(projectDTO), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Project>> findAll(){
        try{
            return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteById(@PathVariable Long id){
        try {
            projectService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }
    }

    @PutMapping
    public ResponseEntity<Project> save(@RequestBody ProjectEditionDTO projectEditionDTO){
        try {
            return new ResponseEntity<>(projectService.save(projectEditionDTO), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
