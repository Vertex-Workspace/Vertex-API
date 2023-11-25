package com.vertex.vertex.personalization.service;

import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.personalization.repository.PersonalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonalizationService {
    private final PersonalizationRepository personalizationRepository;

    public Personalization save(Personalization group){
        return personalizationRepository.save(group);
    }

    public List<Personalization> findAll(){
        return personalizationRepository.findAll();
    }

    public Personalization findById(Long id){
        return personalizationRepository.findById(id).get();
    }

    public void deleteById(Long id){
        personalizationRepository.deleteById(id);
    }



}
