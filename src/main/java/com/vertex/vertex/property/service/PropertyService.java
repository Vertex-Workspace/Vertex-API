package com.vertex.vertex.property.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.DTO.PropertyRegisterDTO;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.model.exceptions.CantCreateOtherStatusException;
import com.vertex.vertex.property.model.exceptions.CantDeleteStatusException;
import com.vertex.vertex.property.model.exceptions.PropertyIsNotAListException;
import com.vertex.vertex.property.repository.PropertyListRepository;
import com.vertex.vertex.property.repository.PropertyRepository;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.model.entity.ValueText;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.vertex.vertex.property.model.ENUM.PropertyKind.*;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyListRepository propertyListRepository;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final ValueService valueService;
    private final ModelMapper mapper;


    public Property save(Long projectID, Property property) {
        Project project = projectService.findById(projectID);
        Property finalProperty = new Property();

        if (property.getId() != 0) {
            mapper.map(property, finalProperty);
            finalProperty.getPropertyLists().forEach(propertyList -> propertyList.setProperty(property));

            if (!property.getDefaultValue().equals("")) {
                project.getTasks().forEach(task ->
                        task.getValues().forEach(value -> {
                            if (value.getProperty().getId().equals(property.getId())) {
                               value.setValue(property.getDefaultValue());
                               valueService.save(value);
                            }
                        })
                );
            }
        } else {
            finalProperty = new Property(PropertyKind.TEXT,
                    "Nova Propriedade", false, "", PropertyStatus.VISIBLE);
            project.getProperties().add(finalProperty);
            for (Task task : project.getTasks()) {
                Value newValue = property.getKind().getValue();
                newValue.setProperty(finalProperty);
                newValue.setTask(task);
                valueService.save(newValue);
            }
        }
        finalProperty.setProject(project);
        return propertyRepository.save(finalProperty);
    }

    //in this method, we need, firstly, remove the value, and then remove the property
    public void delete(Long projectId, Long propertyId) {
        Property property = findById(propertyId);
        Project project = projectService.findById(projectId);


        if (property.getKind() != STATUS &&
                property.getKind() != DATE) {
            propertyRepository.delete(property);
            for (Task task : project.getTasks()) {
                for (Value value : task.getValues()) {
                    if (value.getProperty().getId().equals(property.getId())) {
                        valueService.delete(value);
                    }
                }
            }
        } else {
            throw new CantDeleteStatusException();
        }
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).get();
    }


    public void deletePropertyList(Long propertyID, Long propertyListID) {

        //It validates if the id are correct
        Property property = findById(propertyID);
        PropertyList propertyList = propertyListRepository.findById(propertyListID).get();

        if (property.getPropertyLists().contains(propertyList)) {
            Project project = property.getProject();

            //Pass through all tasks
            for (Task task : project.getTasks()) {
                //Pass through all values inside one task
                for (Value value : task.getValues()) {
                    //Validates and find the value with property ID
                    if (value.getProperty().getId().equals(property.getId())) {
                        //Validates if the value that intend to be excluded, belongs to the current value
                        if (value.getProperty().getPropertyLists().contains(propertyList)) {
                            if (property.getKind() == STATUS) {
                                PropertyList currentPropertyList = (PropertyList) value.getValue();
                                //Set the value as the default for each task
                                value.setValue(getFixedStatusValue(project, currentPropertyList));
                                valueService.save(value);
                            }
//                            if(property.getKind() == LIST){
//
//                            }
                        }
                    }
                }
            }
            property.getPropertyLists().remove(propertyList);
            propertyRepository.save(property);
        }
    }


    //Find the TO DO default of the project
    private PropertyList getFixedStatusValue(Project project, PropertyList currentPropertyList) {
        for (Property property : project.getProperties()) {
            if (property.getKind() == STATUS) {
                for (PropertyList propertyList : property.getPropertyLists()) {
                    if (propertyList.getPropertyListKind() == currentPropertyList.getPropertyListKind() &&
                            propertyList.getIsFixed()) {
                        return propertyList;
                    }
                }
            }
        }
        return null;
    }
}
