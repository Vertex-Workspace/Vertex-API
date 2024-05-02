package com.vertex.vertex.user.relations.personalization.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personalization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String primaryColor;
    private Integer fontSize;
    private String fontFamily;
    private Integer theme;
    private Boolean signLanguage;
    private Boolean listeningText;
    private String language;
    private String linkLanguageImage;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;
}