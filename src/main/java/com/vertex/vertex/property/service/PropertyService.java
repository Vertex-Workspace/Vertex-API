package com.vertex.vertex.property.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.DTO.PropertyRegisterDTO;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.model.exceptions.ProjectDoesNotExistException;
import com.vertex.vertex.property.model.exceptions.PropertyIsNotAListException;
import com.vertex.vertex.property.repository.PropertyRepository;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.vertex.vertex.property.model.ENUM.PropertyKind.LIST;
import static com.vertex.vertex.property.model.ENUM.PropertyKind.STATUS;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final ProjectService projectService;

    public Property save(PropertyRegisterDTO propertyRegisterDTO) {
        Property property = new Property();
        BeanUtils.copyProperties(propertyRegisterDTO, property);

        //When a property is registered, it must be added into property list in Project
        try {
            Project project = projectService.findById(property.getProject().getId());
            project.getProperties().add(property);
            //in this for i, the property is being added in the tasks that already exists in the project
            for (int i = 0; i < project.getTasks().size(); i++) {
                Value newValue = property.getKind().getValue();
                Task task = project.getTasks().get(i);
                newValue.setProperty(property);
                newValue.setTask(task);
                project.getTasks().get(i).getValues().add(newValue);
            }
            property.setPropertyLists(defaultStatus(property));
            propertyRepository.save(property);
        } catch (Exception e) {
            throw new ProjectDoesNotExistException();
        }
        return propertyRepository.save(property);
    }

    //in this method, we need, firstly, remove the value, and then remove the property
    public void delete(Long id) {
        Property property = findById(id);
        Project project = projectService.findById(property.getProject().getId());
        for (Task task : project.getTasks()) {
            for (int i = 0; i < task.getValues().size(); i++) {
                task.getValues().remove(i);
            }
        }
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
                    //verify if the property is a list or status
                    if ((property.getKind() == STATUS) ||
                            (property.getKind() == LIST)) {
                        //if the id doesn't exists, it means that this elements will be saved with a new id
                        property.getPropertyLists().add(list);
                        list.setProperty(property);
                        System.out.println("e");
                    } else {
                        throw new PropertyIsNotAListException();
                    }
                } else {
                    //if the id already exists, it'll be edited
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

    public List<PropertyList> defaultStatus(Property property){
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("to-do default", Color.RED, property, PropertyListKind.TODO));
        propertiesList.add(new PropertyList("doing default", Color.YELLOW, property, PropertyListKind.DOING));
        propertiesList.add(new PropertyList("done default", Color.GREEN, property, PropertyListKind.DONE));
        propertiesList.add(new PropertyList("analysis", Color.PURPLE, property, PropertyListKind.UNDERANALYSIS));
        return propertiesList;
    }
}
