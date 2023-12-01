package com.vertex.vertex.user.relations.personalization.model.entity;


import com.vertex.vertex.user.relations.personalization.relations.fontFamily.model.entity.FontFamily;
import com.vertex.vertex.user.relations.personalization.relations.fontSize.model.entity.FontSize;
import com.vertex.vertex.user.relations.personalization.relations.primaryColor.model.entity.PrimaryColor;
import com.vertex.vertex.user.relations.personalization.relations.secondColor.model.entity.SecondColor;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Personalization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private PrimaryColor primaryColor;

    @ManyToOne(cascade = CascadeType.ALL)
    private SecondColor secondColor;

    @ManyToOne(cascade = CascadeType.ALL)
    private FontSize fontSize;

    @ManyToOne(cascade = CascadeType.ALL)
    private FontFamily fontFamily;

    private Boolean voiceCommand;
    private Boolean listeningText;
    @OneToOne
    private User user;

}