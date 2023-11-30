package com.vertex.vertex.property.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.DTO.PropertyRegisterDTO;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.vertex.vertex.property.model.ENUM.PropertyKind.LIST;
import static com.vertex.vertex.property.model.ENUM.PropertyKind.STATUS;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Property save(PropertyRegisterDTO propertyRegisterDTO) {
        Property property = new Property();
        BeanUtils.copyProperties(propertyRegisterDTO, property);
        return propertyRepository.save(property);
    }

    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).get();
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property save(PropertyListDTO propertyListDTO) {
        Property property;
        try {
            property = propertyRepository.findById(propertyListDTO.getId()).get();
            for (PropertyList list : propertyListDTO.getPropertyLists()) {
                if (list.getId() == null) {
                    if ((property.getKind() == STATUS) ||
                            (property.getKind() == LIST)) {
                        property.getPropertyLists().add(list);
                        list.setProperty(property);
                    } else {
                        throw new RuntimeException("To add a list, it must be a status or a list");
                    }
                } else {
                    for (int cont = 0; cont < property.getPropertyLists().size(); cont++) {
                        if (property.getPropertyLists().get(cont).getId().equals(list.getId())) {
                            property.getPropertyLists().set(cont, list);
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
        return propertyRepository.save(property);
    }

}
