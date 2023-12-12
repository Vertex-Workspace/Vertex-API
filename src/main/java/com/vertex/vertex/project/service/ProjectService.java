package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.model.exception.ProjectDoesNotExistException;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.repository.PropertyRepository;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.model.exceptions.UserNotFoundInTeamException;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final TeamService teamService;
    private final UserTeamService userTeamService;

    public Project save(Project project, Long teamId) {
        Team team;
        Property property = new Property(PropertyKind.STATUS, "STATUS", true, "STATUS");
        try {
            team = teamService.findTeamById(teamId);
        } catch (Exception e) {
            throw new TeamNotFoundException(teamId);
        }
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(teamId, project.getCreator().getId());
        if(userTeam == null){
            throw new UserNotFoundInTeamException();
        }

        project.setCreator(userTeam);
        project.setTeam(team);
        property.setPropertyLists(defaultStatus(property));
        project.addProperty(property);
        property.setProject(project);
        team.getProjects().add(project);
        return projectRepository.save(project);
    }

    public Set<Project> findAll(Long teamID){
        return projectRepository.findAllByTeam_Id(teamID);
    }

    public Project findById(Long id){
        try {
            return projectRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ProjectDoesNotExistException(id);
        }
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

    public List<Project> findAllByTeam(Long teamId) {
        try {
            Team team = teamService.findTeamById(teamId);
            return team.getProjects();

        } catch (Exception e) {
            throw new RuntimeException("Time não encontrado");
        }
    }

    public Boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }

    public Boolean existsByIdAndUserBelongs(Long projectId, Long userId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            Team team = project.getTeam();

            return teamService.findUserInTeam(team, userId);
        }
        return false;
    }

}
