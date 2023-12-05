package com.vertex.vertex.property.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    @Enumerated(value = EnumType.STRING)
    private PropertyKind kind;

    @Column(length = 30)
    private String name;

    private Boolean isObligate;

    private String defaultValue;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "property", orphanRemoval = true)
    private List<PropertyList> propertyLists;
}
