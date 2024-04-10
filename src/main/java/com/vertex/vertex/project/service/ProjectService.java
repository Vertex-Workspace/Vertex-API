package com.vertex.vertex.project.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.project.model.DTO.*;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.property.model.ENUM.Color;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.ENUM.PropertyStatus;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserTeamService userTeamService;
    private final ValueService valueService;
    private final FileService fileService;
    private final NotificationService notificationService;
    private final ModelMapper mapper;

    public Project save(ProjectCreateDTO projectCreateDTO, Long teamId) {
        Project project = new Project();
        mapper.map(projectCreateDTO, project);

        List<UserTeam> collaborators = new ArrayList<>();
        UserTeam userTeam = userTeamService
                .findUserTeamByComposeId(
                        teamId, project.getCreator().getUser().getId());
       Team team = userTeam.getTeam();
        project.setTeam(team);

        if (projectCreateDTO.getUsers() != null) {
            for (User user : projectCreateDTO.getUsers()) {
                UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(teamId, user.getId());
                if (!collaborators.contains(userTeam1)) {
                    collaborators.add(userTeam1);

                    if(userTeam1.getUser().getResponsibleInProjectOrTask()){
                        notificationService.save(new Notification(
                                project,
                                "Você foi adicionado(a) como responsável do projeto " + project.getName(),
                                "projeto/" + project.getId() + "/tarefas",
                                userTeam1.getUser()
                        ));
                    }
                }
            }
        }

        project.setGroups(projectCreateDTO.getGroups());
        project.setCollaborators(collaborators);


        project.setCreator(userTeam);
        if(projectCreateDTO.getProjectReviewENUM() != null) project.setProjectReviewENUM(projectCreateDTO.getProjectReviewENUM());
        else project.setProjectReviewENUM(ProjectReviewENUM.OPTIONAL);

        if (!collaborators.contains(project.getCreator())) {
            collaborators.add(project.getCreator());
        }
        defaultProperties(project);
        return projectRepository.save(project);
    }


    public Set<Project> findAllByTeam(Long teamId) {
        return projectRepository.findAllByTeam_Id(teamId);
    }

    public void updateImage(MultipartFile file, Long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).get();
        File file1 = fileService.save(file);
        project.setFile(file1);
        projectRepository.save(project);
    }

    public Boolean existsByIdAndUserBelongs(Long projectId, Long userId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            return userTeamService.findUserTeamByComposeId(project.getTeam().getId(), userId) != null;
        }
        return false;
    }

    public Project findById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()){
            return optionalProject.get();
        }
        throw new RuntimeException("Project not found");
    }

    public ProjectOneDTO findProjectById(Long id) {
        Project project = findById(id);
        ProjectOneDTO projectOneDTO = new ProjectOneDTO(project);

        //To Set as null
        projectOneDTO.setTasks(new ArrayList<>());

        //Pass through all tasks of the project and validates if task has an opened review (UNDERANALYSIS)
        //If it has, It won't be included into list
        for (Task task : project.getTasks()) {
            boolean isReviewed = false;
            for (Review review : task.getReviews()) {
                if (review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)
                ) {
                    isReviewed = true;
                    break;
                }
            }
            if (!isReviewed) {
                projectOneDTO.getTasks().add(task);
            }
        }
        projectOneDTO.setIdTeam(project.getTeam().getId());
        return projectOneDTO;
    }

    public void deleteById(Long id) {
        Project project = findById(id);
        //Delete every value of tasks on project
        project.getTasks().forEach(task -> task.getValues().forEach(valueService::delete));

        projectRepository.deleteById(id);
    }

    public Project save(Project project) {
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

        for (Property property : properties) {
            if (property.getKind() == PropertyKind.STATUS) {
                property.setPropertyLists(defaultStatus(property));
            }
            if (property.getKind() == PropertyKind.LIST) {
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


    public List<PropertyList> defaultStatus(Property property) {
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("Não Iniciado", Color.RED, property, PropertyListKind.TODO, true));
        propertiesList.add(new PropertyList("Em Andamento", Color.YELLOW, property, PropertyListKind.DOING, true));
        propertiesList.add(new PropertyList("Pausado", Color.BLUE, property, PropertyListKind.DOING, false));
        propertiesList.add(new PropertyList("Concluído", Color.GREEN, property, PropertyListKind.DONE, true));
        return propertiesList;
    }

    public List<Project> getAllByTeamAndCollaborators(Long teamId, Long userId) {
        List<Project> projects = new ArrayList<>();
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(teamId, userId);
        Team team = userTeam.getTeam();

        for (Project project : team.getProjects()) {
            for (UserTeam userTeamFor : project.getCollaborators()) {
                if (userTeam.equals(userTeamFor)) {
                    projects.add(project);
                }
            }
        }
        return projects;
    }

    public List<User> getUsersByProject(Long projectId) {

        List<User> users = new ArrayList<>();
        Project project = findById(projectId);;
        Team team = project.getTeam();

        for (UserTeam userTeam : project.getCollaborators()) {
            if(!team.getGroups().isEmpty()){
                for(Group group: team.getGroups()){
                    if(!group.getUserTeams().contains(userTeam)){
                        users.add(userTeam.getUser());
                    }
                }
            }else {
                users.add(userTeam.getUser());
            }
        }

        return users;
    }


    public Project updateProjectCollaborators(ProjectEditDTO projectEditDTO) {
        Project project = findById(projectEditDTO.getId());

        project.setName(projectEditDTO.getName());
        project.setDescription(projectEditDTO.getDescription());

        List<UserTeam> userTeamsToAdd = new ArrayList<>();

        for (User user : projectEditDTO.getUsers()) {
            UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), user.getId());
            userTeamsToAdd.add(userTeam1);
        }

        for (Boolean bool : List.of(false, true)){
            String title = bool ? "Agora você é responsável do projeto " : "Você não é responsável do projeto ";
            userTeamsToAdd.stream()
                    .filter(userTeam -> project.getCollaborators().contains(userTeam) == bool)
                    .toList()
                    .forEach(userTeam -> {
                        notificationService.save(new Notification(
                            project,
                                title + project.getName(),
                            "projeto/" + project.getId() + "/tarefas",
                            userTeam.getUser()
                    ));
            });
        }
        project.setCollaborators(userTeamsToAdd);
        project.setGroups(projectEditDTO.getGroups());
        project.setProjectReviewENUM(projectEditDTO.getProjectReviewENUM());
        return projectRepository.save(project);
    }

    public List<ProjectSearchDTO> findAllByUserAndQuery(Long userId, String query) {
        return userTeamService.findAllUserTeamByUserId(userId)
                .stream()
                .map(UserTeam::getTeam)
                .flatMap(ut -> ut.getProjects().stream())
                .filter(ut -> ut.getName().toLowerCase().contains(query.toLowerCase()))
                .map(ProjectSearchDTO::new)
                .toList();
    }



    public List<Group> getGroupsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        return project.getGroups();
    }


    public ProjectCollaborators returnAllCollaborators(Long projectId){
        Project project = findById(projectId);
        ProjectCollaborators projectCollaborators = new ProjectCollaborators();
        List<User> users = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<User> userInGroup = new ArrayList<>();

        for(UserTeam userTeam : project.getCollaborators()){
            users.add(userTeam.getUser());
        }
        if(project.getGroups() != null) {
            groups.addAll(project.getGroups());
        }

        if(project.getTeam().getGroups() != null){
            for(Group group : project.getTeam().getGroups()){
                for(UserTeam userTeam : group.getUserTeams()){
                    if(users.contains(userTeam.getUser())){
                        userInGroup.add(userTeam.getUser());
                        users.remove(userTeam.getUser());
                    }
                }
            }
        }

        projectCollaborators.setUserInGroups(userInGroup);
        projectCollaborators.setGroups(groups);
        projectCollaborators.setUsers(users);
        return projectCollaborators;
    }

}
