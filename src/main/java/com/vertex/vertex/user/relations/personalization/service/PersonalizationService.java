package com.vertex.vertex.user.relations.personalization.service;

import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import com.vertex.vertex.user.relations.personalization.relations.fontFamily.repository.FontFamilyRepository;
import com.vertex.vertex.user.relations.personalization.relations.fontSize.repository.FontSizeRepository;
import com.vertex.vertex.user.relations.personalization.relations.primaryColor.model.entity.PrimaryColor;
import com.vertex.vertex.user.relations.personalization.relations.primaryColor.repository.PrimaryColorRepository;
import com.vertex.vertex.user.relations.personalization.relations.secondColor.repository.SecondColorRepository;
import com.vertex.vertex.user.relations.personalization.repository.PersonalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PersonalizationService {

    private final PersonalizationRepository personalizationRepository;
    private final PrimaryColorRepository primaryColorRepository;
    private final SecondColorRepository secondColorRepository;
    private final FontFamilyRepository fontFamilyRepository;
    private final FontSizeRepository fontSizeRepository;

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
        personalization.setPrimaryColor(primaryColorRepository.findById(1L).get());
        personalization.setSecondColor(secondColorRepository.findById(1L).get());
        personalization.setFontFamily(fontFamilyRepository.findById(1L).get());
        personalization.setFontSize(fontSizeRepository.findById(1L).get());
        personalization.setVoiceCommand(false);
        personalization.setListeningText(false);
        personalization.setUser(user);

        return personalization;
    }

    public Personalization patchUserPersonalization(User user,Personalization personalization){
        personalization.setPrimaryColor(primaryColorRepository.findById(personalization.getPrimaryColor().getId()).get());
        personalization.setSecondColor(secondColorRepository.findById(personalization.getSecondColor().getId()).get());
        personalization.setFontFamily(fontFamilyRepository.findById(personalization.getFontFamily().getId()).get());
        personalization.setFontSize(fontSizeRepository.findById(personalization.getFontSize().getId()).get());
        personalization.setVoiceCommand(personalization.getVoiceCommand());
        personalization.setListeningText(personalization.getListeningText());
        personalization.setUser(user);

        System.out.println(personalization);

        return personalization;
    }

    public List<Personalization> findAll() {
        return personalizationRepository.findAll();
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



}