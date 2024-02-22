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
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.task.service.TaskService;
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
    private final ValueService valueService;

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
        Project project = findById(id);
        //Delete every value of tasks on project
        project.getTasks().forEach(task -> task.getValues().forEach(valueService::delete));

        projectRepository.deleteById(id);
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }

    public List<PropertyList> defaultStatus(Property property){
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("Não Iniciado", Color.RED, property, PropertyListKind.TODO, true));
        propertiesList.add(new PropertyList("Em Andamento", Color.YELLOW, property, PropertyListKind.DOING, true));
        propertiesList.add(new PropertyList("Concluído", Color.GREEN, property, PropertyListKind.DONE, true));
        return propertiesList;
    }
}
