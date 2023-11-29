package com.vertex.vertex.personalization.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.personalization.subPersonalization.fontFamily.model.entity.FontFamily;
import com.vertex.vertex.personalization.subPersonalization.fontSize.model.entity.FontSize;
import com.vertex.vertex.personalization.subPersonalization.primaryColor.model.entity.PrimaryColor;
import com.vertex.vertex.personalization.subPersonalization.secondColor.model.entity.SecondColor;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Personalization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private PrimaryColor primaryColor;

    @OneToOne(cascade = CascadeType.ALL)
    private SecondColor secondColor;

    @OneToOne(cascade = CascadeType.ALL)
    private FontSize fontSize;

    @OneToOne(cascade = CascadeType.ALL)
    private FontFamily fontFamily;

    private Boolean voiceCommand;
    private Boolean listeningText;
    @OneToOne
    private User user;

}