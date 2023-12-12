package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.model.exception.ProjectDoesNotExistException;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.model.exceptions.UserNotFoundInTeamException;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
