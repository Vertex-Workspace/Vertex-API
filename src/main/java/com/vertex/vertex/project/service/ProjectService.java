package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final TeamService teamService;
    private final UserTeamService userTeamService;

    public Project save(Project project, Long teamId) {
        Team team = null;
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
            Team team = project.getTeam();

            return teamService.findUserInTeam(team, userId);
        }
        return false;
    }

    public Project findById(Long id){
        return projectRepository.findById(id).get();
    }

    public void deleteById(Long id){
        projectRepository.deleteById(id);
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }
}
