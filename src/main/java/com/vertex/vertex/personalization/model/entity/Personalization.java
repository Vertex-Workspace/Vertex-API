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
    @Column(columnDefinition = "#092C4C", nullable = false)
    private String primaryColor;
    @Column(columnDefinition = "#F3F3F3", nullable = false)
    private String secondColor;
    @Column(columnDefinition = "12", nullable = false)
    private Integer fontSize;
    @Column(columnDefinition = "Inter", nullable = false)
    private String fontFamily;
    private Boolean voiceCommand;
    private Boolean listeningText;
    @OneToOne
    private User user;

}
