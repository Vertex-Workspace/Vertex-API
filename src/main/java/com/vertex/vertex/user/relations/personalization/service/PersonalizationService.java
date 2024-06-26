package com.vertex.vertex.user.relations.personalization.service;

import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.repository.PersonalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PersonalizationService {

    private final PersonalizationRepository personalizationRepository;

//    public Personalization save(Personalization personalization) {
//        return personalizationRepository.save(personalization);
//    }
//
//    public Personalization put(Personalization personalization) {
//        if (personalization.getId()!= null && !personalizationRepository.existsById(personalization.getId())) {
//            throw new RuntimeException("A personalização não existe.");
//        }
//        return personalizationRepository.save(personalization);
//    }

    public Personalization defaultSave(User user){
        Personalization personalization = new Personalization();
        personalization.setPrimaryColor("#092C4C");
        personalization.setFontFamily("Inter");
        personalization.setFontSize(14);
        personalization.setTheme(0);
        personalization.setSignLanguage(false);
        personalization.setListeningText(false);
        personalization.setUser(user);
        personalization.setLanguage("pt");
        return personalization;
    }

    public Personalization findById(Long id) {
        if (!personalizationRepository.existsById(id)) {
            throw new NoSuchElementException("Personalização escolhida não existe.");
        }
        return personalizationRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        if (!personalizationRepository.existsById(id)) {
            throw new NoSuchElementException("Personalização escolhida para deletar não existe.");
        } else {
            personalizationRepository.deleteById(id);
        }
    }

    public Personalization findByUserId(Long id){
        return personalizationRepository.findByUser_Id(id);
    }



}