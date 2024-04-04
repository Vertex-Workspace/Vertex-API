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
        personalization.setPrimaryColorLight("#092C4C");
        personalization.setSecondColorLight("#F3F3F3");
        personalization.setPrimaryColorDark("#F3F3F3");
        personalization.setSecondColorDark("#1E1E1E");
        personalization.setFontFamily("Inter");
        personalization.setFontSize(14);
        personalization.setTheme(0);
        personalization.setSignLanguage(false);
        personalization.setListeningText(false);
        personalization.setUser(user);

        return personalization;
    }

//    public User patchUserPersonalization(User user, Personalization personalization){
//
////        user.getPersonalization().setPrimaryColor(personalization.getPrimaryColor());
////        user.getPersonalization().setSecondColor(personalization.getSecondColor());
////        user.getPersonalization().setFontFamily(personalization.getFontFamily());
////        user.getPersonalization().setFontSize(personalization.getFontSize());
////        user.getPersonalization().setVoiceCommand(personalization.getVoiceCommand());
////        user.getPersonalization().setListeningText(personalization.getListeningText());
////        user.getPersonalization().setUser(personalization.getUser());
//
//
//        return personalization;
//    }

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