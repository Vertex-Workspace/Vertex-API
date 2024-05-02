package com.vertex.vertex.team.relations.permission.controller;

import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/team/permission")
public class PermissionController {

    private final PermissionService permissionService;


    @PatchMapping("/{permissionId}")
    public ResponseEntity<?> giveAPermission(@PathVariable Long permissionId){
        try{
            permissionService.changeEnabled(permissionId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
