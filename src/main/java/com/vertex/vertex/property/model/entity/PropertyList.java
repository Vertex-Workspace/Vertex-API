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

    private String color;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Property property;


    private Boolean isFixed;

    @Enumerated(value = EnumType.STRING)
    private PropertyListKind propertyListKind;

    public PropertyList(String value, Color color, Property property, PropertyListKind propertyListKind, Boolean isFixed) {
        this.value = value;
        this.color = color.getHexadecimal();
        this.property = property;
        this.propertyListKind = propertyListKind;
        this.isFixed = isFixed;
    }
}
