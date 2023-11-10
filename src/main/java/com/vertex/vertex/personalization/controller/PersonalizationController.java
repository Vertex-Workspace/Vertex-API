package com.vertex.vertex.personalization.controller;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.personalization.service.PersonalizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personalization")
public class PersonalizationController {
    private final PersonalizationService personalizationService;

    public PersonalizationController(PersonalizationService personalizationService) {
        this.personalizationService = personalizationService;
    }

    @PutMapping
    public ResponseEntity<Personalization> save(@RequestBody Personalization personalization){
        return new ResponseEntity<>(personalizationService.save(personalization), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Personalization> edit(@RequestBody Personalization personalization){
        return new ResponseEntity<>(personalizationService.save(personalization), HttpStatus.OK);
    }

}
