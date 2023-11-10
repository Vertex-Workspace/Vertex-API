package com.vertex.vertex.personalization.service;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.personalization.repository.PersonalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonalizationService {
    private final PersonalizationRepository personalizationRepository;

    public Personalization save(Personalization personalization){
        return personalizationRepository.save(personalization);
    }



}
