package com.vertex.vertex.personalization.service;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.personalization.repository.PersonalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@AllArgsConstructor
public class PersonalizationService {
    private final PersonalizationRepository personalizationRepository;

    @PutMapping
    public ResponseEntity<Personalization> save(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
