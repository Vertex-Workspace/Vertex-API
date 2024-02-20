package com.vertex.vertex.property.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.DTO.PropertyRegisterDTO;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.model.exceptions.CantCreateOtherStatusException;
import com.vertex.vertex.property.model.exceptions.CantDeleteStatusException;
import com.vertex.vertex.property.model.exceptions.PropertyIsNotAListException;
import com.vertex.vertex.property.repository.PropertyRepository;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.service.ValueService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.vertex.vertex.property.model.ENUM.PropertyKind.*;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final ProjectService projectService;
    private final ValueService valueService;
    private final ProjectRepository projectRepository;

    private final ModelMapper mapper;


    public Property save(Long projectID, Property property) {
        Project project = projectService.findById(projectID);
        Property finalProperty = new Property();

        if(property.getId() != 0){
            mapper.map(property, finalProperty);
            finalProperty.setProject(project);

            //Property Lists
            savePropertyList(property);

            for ( Property propertyFor : project.getProperties()) {
                if(propertyFor.getId().equals(property.getId())){
                    project.getProperties().set(project.getProperties().indexOf(propertyFor), finalProperty);
                    break;
                }
            }
        } else {
            finalProperty  = new Property(PropertyKind.TEXT,
                    "Nova Propriedade", false, "", PropertyStatus.VISIBLE);
            finalProperty.setProject(project);
            project.getProperties().add(finalProperty);
        }

        try{
            projectRepository.save(project);
            return finalProperty;
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //in this method, we need, firstly, remove the value, and then remove the property
    public void delete(Long projectId, Long propertyId) {
        Value valueToDelete;
        Property property = findById(propertyId);
        Project project = projectService.findById(projectId);
        if (property.getKind() != STATUS &&
                property.getKind() != DATE) {
            for (Task task : project.getTasks()) {
                for (int i = 0; i < task.getValues().size(); i++) {
                    if (task.getValues().get(i).getProperty().getId().equals(propertyId)) {
                        valueToDelete = task.getValues().get(i);
                        task.getValues().remove(valueToDelete);
                        valueService.delete(valueToDelete);
                        propertyRepository.deleteById(propertyId);
                    }
                }
            }
            projectRepository.save(project);
        } else {
            throw new CantDeleteStatusException();
        }
        boolean isRemoved = false;
        for ( Property propertyFor : project.getProperties()) {
            if(propertyFor.getId().equals(propertyId)){
                project.getProperties().remove(propertyFor);
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            throw new RuntimeException("There isn't a project with this property id or there isn't a property with this project id");
        }
        projectRepository.save(project);
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).get();
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    private void savePropertyList(Property property) {
        try {
            for (PropertyList list : property.getPropertyLists()) {
                list.setProperty(property);
                if(list.getId() != 0) {
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
            throw new RuntimeException();
        }
    }
}
