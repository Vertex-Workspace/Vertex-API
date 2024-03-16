package com.vertex.vertex.project.service;

import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
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
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserTeamService userTeamService;
    private final ValueService valueService;
    private final FileService fileService;

    public Project save(ProjectCreateDTO projectCreateDTO, Long teamId){
        UserTeam userTeam;
        Team team;
        Project project = new Project();
        BeanUtils.copyProperties(projectCreateDTO, project);

        List<UserTeam> collaborators = new ArrayList<>();

        if(projectCreateDTO.getListOfResponsibles() != null){
            for(User user : projectCreateDTO.getListOfResponsibles()){
                UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(teamId, user.getId());
                if(!collaborators.contains(userTeam1)){
                    collaborators.add(userTeam1);
                }
            }
        }

        project.setCollaborators(collaborators);

        userTeam = userTeamService
                .findUserTeamByComposeId(
                        teamId, project.getCreator().getUser().getId());
        team = userTeam.getTeam();
        project.setCreator(userTeam);
        project.setTeam(team);
        defaultProperties(project);
        projectRepository.save(project);
        if(!collaborators.contains(project.getCreator())) {
            collaborators.add(project.getCreator());
        }
        return projectRepository.save(project);
    }

    public void defaultProperties(Project project) {

        //Default properties of a project
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(PropertyKind.STATUS, "Status", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.DATE, "Data", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.LIST, "Dificuldade", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.NUMBER, "Número", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.TEXT, "Palavra-Chave", false, null, PropertyStatus.INVISIBLE));

        for ( Property property : properties ) {
            if(property.getKind() == PropertyKind.STATUS){
                property.setPropertyLists(defaultStatus(property));
            }
            if(property.getKind() == PropertyKind.LIST){
                List<PropertyList> propertiesList = new ArrayList<>();
                propertiesList.add(new PropertyList("Fácil", Color.GREEN, property, PropertyListKind.VISIBLE, false));
                propertiesList.add(new PropertyList("Médio", Color.YELLOW, property, PropertyListKind.VISIBLE, true));
                propertiesList.add(new PropertyList("Díficil", Color.RED, property, PropertyListKind.VISIBLE, true));
                propertiesList.add(new PropertyList("Não validado", Color.BLUE, property, PropertyListKind.INVISIBLE, true));
                property.setPropertyLists(propertiesList);
            }
            property.setProject(project);
            project.addProperty(property);
        }
    }

    public void updateImage(MultipartFile file, Long projectId) {
        try {
            Project project = findById(projectId);
            project.setImage(file.getBytes());
            projectRepository.save(project);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

//    public void updateImage(MultipartFile file, Long projectId) throws IOException {
//        System.out.println(Arrays.toString(file.getBytes()));
//        Project project = projectRepository.findById(projectId).get();
//        project.setImage(file.getBytes());
//        projectRepository.save(project);
//        fileService.updateImageProject(file, projectId);
//    }

    public Boolean existsByIdAndUserBelongs(Long projectId, Long userId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            return userTeamService.findUserInTeam(project.getTeam(), userId);
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
        propertiesList.add(new PropertyList("Pausado", Color.BLUE, property, PropertyListKind.DOING, false));
        propertiesList.add(new PropertyList("Concluído", Color.GREEN, property, PropertyListKind.DONE, true));
        return propertiesList;
    }

    public List<Project> getAllByTeamAndCollaborators(Long teamId , Long userId){
        List<Project> projects = new ArrayList<>();
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(teamId, userId);
        Team team = userTeam.getTeam();

        for(Project project : team.getProjects()){
            for(UserTeam userTeamFor : project.getCollaborators()){
                if(userTeam.equals(userTeamFor)){
                    projects.add(project);
                }
            }
        }
        return projects;
    }

}
