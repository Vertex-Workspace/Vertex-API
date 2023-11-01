package com.vertex.vertex.project.service;

import com.vertex.vertex.project.model.DTO.ProjectDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditionDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project save(ProjectDTO projectDTO){
        Project project = new Project();

        BeanUtils.copyProperties(projectDTO, project);
        return projectRepository.save(project);
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
