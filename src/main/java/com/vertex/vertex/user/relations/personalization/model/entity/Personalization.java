package com.vertex.vertex.user.relations.personalization.model.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vertex.vertex.user.relations.personalization.relations.fontFamily.model.entity.FontFamily;
import com.vertex.vertex.user.relations.personalization.relations.fontSize.model.entity.FontSize;
import com.vertex.vertex.user.relations.personalization.relations.primaryColor.model.entity.PrimaryColor;
import com.vertex.vertex.user.relations.personalization.relations.secondColor.model.entity.SecondColor;
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

    @ManyToOne
    private PrimaryColor primaryColor;

    @ManyToOne
    private SecondColor secondColor;

    @ManyToOne
    private FontSize fontSize;

    @ManyToOne
    private FontFamily fontFamily;

    private Boolean voiceCommand;
    private Boolean listeningText;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;
}