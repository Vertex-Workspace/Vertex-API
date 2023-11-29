package com.vertex.vertex.personalization.subPersonalization.primaryColor.model.entity;

import com.vertex.vertex.personalization.model.entity.Personalization;
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


}
