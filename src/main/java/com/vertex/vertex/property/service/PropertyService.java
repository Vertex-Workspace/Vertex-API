package com.vertex.vertex.property.service;

import com.vertex.vertex.project.model.DTO.ProjectOneDTO;
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
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.model.entity.ValueDate;
import com.vertex.vertex.task.relations.value.model.entity.ValueText;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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


    public ProjectOneDTO save(Long projectID, Property property) {
        Project project = projectService.findById(projectID);
        Property finalProperty = new Property();
        mapper.map(property, finalProperty);

        finalProperty.setProject(project);
        finalProperty.setIsObligate(false);
        finalProperty.getPropertyLists().forEach(propertyList -> propertyList.setProperty(finalProperty));
        Property newProperty = propertyRepository.save(finalProperty);

        for (Task task : project.getTasks()) {
            Value newValue = property.getKind().getValue();
            newValue.setProperty(newProperty);
            newValue.setTask(task);
            if(property.getDefaultValue() != null && !property.getDefaultValue().isEmpty()){
                newValue.setValue(property.getDefaultValue());
            }
            task.getValues().add(valueService.save(newValue));
        }
        taskRepository.saveAll(project.getTasks());
        project.getProperties().add(newProperty);
        return projectService.findProjectById(projectService.save(project).getId());
    }

    public ProjectOneDTO edit(Long projectId, Property property){
        Project project = projectService.findById(projectId);
        Property finalProperty = new Property();
        mapper.map(property, finalProperty);

        finalProperty.setProject(project);

        //Edit Property list
        finalProperty.getPropertyLists().forEach(propertyList -> propertyList.setProperty(property));
        propertyRepository.save(finalProperty);

        return projectService.findProjectById(project.getId());
    }


    //in this method, we need, firstly, remove the value, and then remove the property
    public ProjectOneDTO delete(Long projectId, Long propertyId) {
        Property property = findById(propertyId);
        Project project = projectService.findById(projectId);

        if(!property.getProject().getId().equals(projectId)){
            throw new RuntimeException("Propriedade não pertence ao projeto!");
        }

        if (property.getPropertyStatus() != PropertyStatus.FIXED) {
            this.deleteValuesCascade(project, property);
            propertyRepository.delete(property);
            project.getProperties().remove(property);
            return projectService.findProjectById(projectService.save(project).getId());
        } else {
            throw new CantDeleteStatusException();
        }
    }

    private void deleteValuesCascade(Project project, Property property){
        List<Task> tasksToBeSaved = new ArrayList<>();
        List<Value> values = new ArrayList<>();
        for (Task task : project.getTasks()) {
            for (Value value : task.getValues()) {
                if (value.getProperty().getId().equals(property.getId())) {
                    values.add(value);
                }
            }
            for(Value value : values){
                task.getValues().remove(value);
                tasksToBeSaved.add(task);
            }
        }
        taskRepository.saveAll(tasksToBeSaved);
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).get();
    }


    public Project deletePropertyList(Long propertyID, Long propertyListID) {

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
                        if (value.getProperty().getPropertyLists().contains(propertyList) && property.getKind() == STATUS) {
                            PropertyList currentPropertyList = (PropertyList) value.getValue();
                            //Set the value as the default for each task
                            value.setValue(getFixedStatusValue(project, currentPropertyList));
                            valueService.save(value);
//                            if(property.getKind() == LIST){
//
//                            }
                        }
                    }
                }
            }
            property.getPropertyLists().remove(propertyList);
            propertyRepository.save(property);
            return projectService.findById(property.getProject().getId());
        }
        throw new RuntimeException("The property list doesn't belong to the property");
    }

    public Property changePropertyListColor(PropertyList propertyList) {
        PropertyList currentPropertyList = propertyListRepository.findById(propertyList.getId()).get();
        currentPropertyList.setColor(propertyList.getColor());

        PropertyList propertyListSaved = propertyListRepository.save(currentPropertyList);

        return propertyListSaved.getProperty();
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

    public String getPropertyValueAsString(Property property, Task task) {
        Value value = task.getValues()
                .stream()
                .filter(v -> Objects.equals(property.getId(), v.getProperty().getId()))
                .findFirst()
                .get();

        if (property.getKind() == PropertyKind.STATUS
                || property.getKind() == PropertyKind.LIST) {
            PropertyList pl = (PropertyList) value.getValue();
            return pl.getValue();
        }

        if (value instanceof ValueDate) {
            return ((ValueDate) value).format();
        }

        return value.getValue().toString();
    }

    public Property findByIdAndProjectContains(Long id, Project project) {
        Property property = findById(id);

        if (!project.getProperties().contains(property)) {
            throw new RuntimeException("Property doesn't exist in this project");
        }

        return property;
    }


}