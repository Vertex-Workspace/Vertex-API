package com.vertex.vertex.property.model.DTO;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.PropertyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRegisterDTO {

    private PropertyKind kind;

    private String name;

    private Boolean isObligate;

    private String defaultValue;

    private Project project;

    private PropertyStatus propertyStatus;

    private List<PropertyList> propertyLists;
}
