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
    private String secondColor;
    private Integer fontSize;
    private String fontFamily;
    private String theme;
    private Boolean voiceCommand;
    private Boolean listeningText;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;
}