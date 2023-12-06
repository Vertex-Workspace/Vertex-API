package com.vertex.vertex.property.model.DTO;

import com.vertex.vertex.property.model.entity.PropertyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropertyListDTO {

    //property Id
    private Long id;
    private List<PropertyList> propertyLists;

}
