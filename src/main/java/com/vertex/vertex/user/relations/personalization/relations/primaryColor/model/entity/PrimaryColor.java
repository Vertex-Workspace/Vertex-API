package com.vertex.vertex.user.relations.personalization.relations.primaryColor.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PrimaryColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String primaryColor;

    public PrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }
}
