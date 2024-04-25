package com.vertex.vertex.user.relations.personalization.controller;

import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.service.PersonalizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin
public class PersonalizationController {

    private final PersonalizationService personalizationService;

    @GetMapping("/personalization/{userId}")
    public ResponseEntity<Personalization> findByUserId(@PathVariable Long userId){
        try {
            System.out.println("AQUIIII"+userId);
            return new ResponseEntity<>(personalizationService.findByUserId(userId), HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("Essa personalização não está atrelada à nenhum usuário.");
        }
    }

}
