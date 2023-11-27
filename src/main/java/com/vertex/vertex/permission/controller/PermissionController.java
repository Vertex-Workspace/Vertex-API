package com.vertex.vertex.permission.controller;

import com.vertex.vertex.permission.model.entity.Permission;
import com.vertex.vertex.permission.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/{userTeamId}")
    public ResponseEntity<Permission> create(
            @RequestBody Permission permissionUser,
            @PathVariable Long userTeamId){
        try{
            return new ResponseEntity<>
                    (permissionService.save(permissionUser, userTeamId),
                            HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{userTeamId}")
    public ResponseEntity<Permission> edit(
            @RequestBody Permission permissionUser,
            @PathVariable Long userTeamId){
        try{
            return new ResponseEntity<>
                    (permissionService.save(permissionUser, userTeamId),
                            HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Permission>> findAll(){
        return new ResponseEntity<>
                (permissionService.findAll(),
                        HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> findById(
            @PathVariable Long id){
        try{
            return new ResponseEntity<>
                    (permissionService.findById(id),
                            HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(
            @PathVariable Long id){
        permissionService.deleteById(id);
    }
}
