package com.vertex.vertex.personalization.model.entity;


import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Boolean voiceCommand;
    private Boolean listeningText;
    @OneToOne
    private User user;

}
