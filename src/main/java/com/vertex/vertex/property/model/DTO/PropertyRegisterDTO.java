package com.vertex.vertex.property.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRegisterDTO {
    private PropertyKind kind;

    private String name;

    private Boolean isObligate;

    private String defaultValue;

    private Project project;
}
