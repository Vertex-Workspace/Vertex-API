package com.vertex.vertex.personalization.controller;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.personalization.service.PersonalizationService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/personalization")
public class PersonalizationController {
    private final PersonalizationService personalizationService;

//    @PutMapping
//    public ResponseEntity<Personalization> put(@RequestBody Personalization personalization) {
//        try {
//            return new ResponseEntity<>(personalizationService.put(personalization), HttpStatus.OK);
//        } catch (Exception e) {
////            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<Personalization> save(@RequestBody Personalization personalization) {
//        return new ResponseEntity<>(personalizationService.save(personalization), HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Personalization> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(personalizationService.findById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Personalization>> findAll() {
        return new ResponseEntity<>(personalizationService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Personalization> deleteById(@PathVariable Long id) {
        try {
            personalizationService.deleteById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return null;
    }

}
