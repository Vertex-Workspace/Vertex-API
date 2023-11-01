package com.vertex.vertex.property.service;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;


    public Property save(Property property){
        return propertyRepository.save(property);
    }

    public void delete(Long id){
        propertyRepository.deleteById(id);
    }

    public Property findOne(Long id){
        return propertyRepository.findById(id).get();
    }

    public List<Property> findAll(){
        return propertyRepository.findAll();
    }
}
