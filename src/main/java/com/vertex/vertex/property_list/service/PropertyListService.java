package com.vertex.vertex.property_list.service;

import com.vertex.vertex.personalization.model.entity.Personalization;
import com.vertex.vertex.property_list.model.entity.PropertyList;
import com.vertex.vertex.property_list.repository.PropertyListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropertyListService {
    private final PropertyListRepository propertyListRepository;
    public PropertyList save(PropertyList propertyList){
        return propertyListRepository.save(propertyList);
    }

    public List<PropertyList> findAll(){
        return propertyListRepository.findAll();
    }

    public PropertyList findById(Long id){
        return propertyListRepository.findById(id).get();
    }

    public void deleteById(Long id){
        propertyListRepository.deleteById(id);
    }
}
