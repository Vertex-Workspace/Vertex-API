package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.DTO.ProjectDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditionDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamService teamService;

    public Project save(ProjectDTO projectDTO, Long teamId) {
        Team team;
        Project project = new Project();

        if (teamService.existsById(teamId)) {
            team = teamService.findById(teamId);
            BeanUtils.copyProperties(projectDTO, project);
            project.setTeam(team);
            team.getProjects().add(project);
            return projectRepository.save(project);
        }
        throw new EntityNotFoundException();
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project findById(Long id){
        return projectRepository.findById(id).get();
    }

    public void deleteById(Long id){
        projectRepository.deleteById(id);
    }

    public Project save(ProjectEditionDTO projectEditionDTO){
        Project project = new Project();

        BeanUtils.copyProperties(projectEditionDTO, project);
        return projectRepository.save(project);
    }
}
