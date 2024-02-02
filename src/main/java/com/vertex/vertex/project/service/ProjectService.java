package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.DTO.ProjectOneDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final TeamService teamService;
    private final UserTeamService userTeamService;

    private final ModelMapper mapper = new ModelMapper();

    public Project save(Project project, Long teamId) {
        Team team;
        Property property = new Property(PropertyKind.STATUS, "Status", true, "", PropertyStatus.FIXED);
        Property propertyDate = new Property(PropertyKind.DATE, "Data", true, "", PropertyStatus.FIXED);
        try {
            team = teamService.findTeamById(teamId);
        } catch (Exception e) {
            throw new EntityNotFoundException("There isn't a team with this id!");
        }
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(teamId, project.getCreator().getId());
        if(userTeam == null){
            //after we have to create the exception
            throw new RuntimeException("The user isn't in the team!");
        }
        project.setCreator(userTeam);
        project.setTeam(team);
        property.setPropertyLists(defaultStatus(property));
        project.addProperty(property);
        project.addProperty(propertyDate);
        property.setProject(project);
        propertyDate.setProject(project);
        team.getProjects().add(project);

        return projectRepository.save(project);
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Set<Project> findAllByTeam(Long teamId){
        return projectRepository.findAllByTeam_Id(teamId);
    }

    public boolean existsById(Long projectId){
        try{
            findById(projectId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean existsByIdAndUserBelongs(Long projectId, Long userId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            return teamService.findUserInTeam(project.getTeam(), userId);
        }
        return false;
    }
    public Project findById(Long id){
        return projectRepository.findById(id).get();
    }

    public ProjectOneDTO findProjectById(Long id){
        ProjectOneDTO projectOneDTO = new ProjectOneDTO();
        Project project = projectRepository.findById(id).get();
        BeanUtils.copyProperties(project, projectOneDTO);
        projectOneDTO.setIdTeam(project.getTeam().getId());
        return projectOneDTO;
    }

    public void deleteById(Long id){
        projectRepository.deleteById(id);
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }

    public List<PropertyList> defaultStatus(Property property){
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("to-do default", Color.RED, property, PropertyListKind.TODO));
        propertiesList.add(new PropertyList("doing default", Color.YELLOW, property, PropertyListKind.DOING));
        propertiesList.add(new PropertyList("done default", Color.GREEN, property, PropertyListKind.DONE));
        return propertiesList;
    }


    public Property saveProperty(Long projectID, Property property){
        Project project = findById(projectID);

        Property finalProperty = new Property();

        if(property.getId() != 0){
            mapper.map(property, finalProperty);
            finalProperty.setProject(project);
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

    public Project deleteProperty(Long projectId, Long propertyId){
        Project project = findById(projectId);

        boolean isRemoved = false;
        for ( Property property : project.getProperties()) {
            if(property.getId().equals(propertyId)){
                project.getProperties().remove(property);
                isRemoved = true;
                break;
            }
        }
        if(!isRemoved){
            throw new RuntimeException("There isn't a project with this property id or there isn't a property with this project id");
        }
        return projectRepository.save(project);
    }
}
