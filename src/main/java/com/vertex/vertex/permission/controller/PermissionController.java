package com.vertex.vertex.permission.controller;

import com.vertex.vertex.permission.model.entity.PermissionUser;
import com.vertex.vertex.permission.service.PermissionService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionUser> create(@RequestBody PermissionUser permissionUser){
        try{
            return new ResponseEntity<>(permissionService.save(permissionUser), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<PermissionUser> edit(@RequestBody PermissionUser permissionUser){
        try{
            return new ResponseEntity<>(permissionService.save(permissionUser), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<PermissionUser>> findAll(){
        return new ResponseEntity<>(permissionService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionUser> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(permissionService.findById(id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        permissionService.deleteById(id);
    }
}
