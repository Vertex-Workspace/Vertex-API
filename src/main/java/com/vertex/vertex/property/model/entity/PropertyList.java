package com.vertex.vertex.property.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PropertyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @Enumerated(value = EnumType.STRING)
    private Color color;
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Property property;
    @Enumerated(value = EnumType.STRING)
    private PropertyListKind propertyListKind;

    public PropertyList(String value, Color color, Property property, PropertyListKind propertyListKind) {
        this.value = value;
        this.color = color;
        this.property = property;
        this.propertyListKind = propertyListKind;
    }
}
