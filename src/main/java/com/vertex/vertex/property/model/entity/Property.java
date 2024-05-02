package com.vertex.vertex.property.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(value = EnumType.STRING)
    private PropertyStatus propertyStatus;

    public Property(PropertyKind kind, String name, Boolean isObligate, String defaultValue, PropertyStatus propertyStatus) {
        this.kind = kind;
        this.name = name;
        this.isObligate = isObligate;
        this.defaultValue = defaultValue;
        this.propertyStatus = propertyStatus;
    }
}
