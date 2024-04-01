package com.vertex.vertex.project.service;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.DTO.ProjectEditDTO;
import com.vertex.vertex.project.model.DTO.ProjectOneDTO;
import com.vertex.vertex.project.model.DTO.ProjectSearchDTO;
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
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
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
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    public Project save(ProjectCreateDTO projectCreateDTO, Long teamId) {
        UserTeam userTeam;
        Team team;
        Project project = new Project();
        BeanUtils.copyProperties(projectCreateDTO, project);

        List<UserTeam> collaborators = new ArrayList<>();

        if (projectCreateDTO.getListOfResponsibles() != null) {
            for (User user : projectCreateDTO.getListOfResponsibles()) {
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

        project.setCollaborators(collaborators);

        userTeam = userTeamService
                .findUserTeamByComposeId(
                        teamId, project.getCreator().getUser().getId());
        team = userTeam.getTeam();

        project.setCreator(userTeam);
        project.setTeam(team);
        project.setProjectReviewENUM(projectCreateDTO.getProjectReviewENUM());

        projectRepository.save(project);
        if (!collaborators.contains(project.getCreator())) {
            collaborators.add(project.getCreator());
        }
        defaultProperties(project);
        return projectRepository.save(project);
    }


//    public Project updateImage(MultipartFile file, Long projectId) {
//        try {
//            Project project = findById(projectId);
//            project.setImage(file.getBytes());
//
//            return projectRepository.save(project);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Set<Project> findAllByTeam(Long teamId) {
        return projectRepository.findAllByTeam_Id(teamId);
    }

    public boolean existsById(Long projectId) {
        try {
            findById(projectId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateImage(MultipartFile file, Long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).get();
        File file1 = fileService.save(file);
//        project.setImage(file.getBytes());
        project.setFile(file1);
        projectRepository.save(project);
    }

    public Boolean existsByIdAndUserBelongs(Long projectId, Long userId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            return userTeamService.findUserInTeam(project.getTeam(), userId);
        }
        return false;
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).get();
    }

    public ProjectOneDTO findProjectById(Long id) {
        ProjectOneDTO projectOneDTO = new ProjectOneDTO();
        Project project = projectRepository.findById(id).get();
        BeanUtils.copyProperties(project, projectOneDTO);

        //To Set as null
        projectOneDTO.setTasks(new ArrayList<>());

        //Pass through all tasks of the project and validates if task has an opened review (UNDERANALYSIS)
        //If it has, It won't be included into list
        for (Task task : project.getTasks()) {
            boolean isReviewed = false;
            for (Review review : task.getReviews()){
                if(review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)
                ){
                    isReviewed = true;
                }
            }
            if(!isReviewed){
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
        Project project = projectRepository.findById(projectId).get();
        for (UserTeam userTeam : project.getCollaborators()) {
            users.add(userTeam.getUser());
        }
        return users;
    }


    public Project updateProjectCollaborators(ProjectEditDTO projectEditDTO) {
        Project project = projectRepository.findById(projectEditDTO.getId()).get();
        project.setName(projectEditDTO.getName());
        project.setDescription(projectEditDTO.getDescription());

        if (projectEditDTO.getListOfResponsibles() != null) {
            for (User user : projectEditDTO.getListOfResponsibles()) {
//                if the user selected, it means that it was in project and now it won't be more
                UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), user.getId());
                if (!project.getCollaborators().contains(userTeam1)) {
                    project.getCollaborators().add(userTeam1);
                }else {
                    Iterator<UserTeam> collaboratorsIterator = project.getCollaborators().iterator();
                    while (collaboratorsIterator.hasNext()) {
                        UserTeam userTeam = collaboratorsIterator.next();
                        if (userTeam != null && userTeam.getUser().equals(user)) {
                            if(userTeam.getUser().getResponsibleInProjectOrTask()){
                                notificationService.save(new Notification(
                                        project,
                                        "Você não é mais responsável do projeto " + project.getName(),
                                        "projeto/" + project.getId() + "/tarefas",
                                        userTeam1.getUser()
                                ));
                            }
                            userTeam.setProject(null);
                            userTeamService.save(userTeam);
                            collaboratorsIterator.remove();
                        }
                    }
                }
            }
        }
        project.setProjectReviewENUM(projectEditDTO.getProjectReviewENUM());

        if(projectEditDTO.getProjectReviewENUM() == ProjectReviewENUM.MANDATORY){
            project.getTasks().forEach(task -> task.setRevisable(true));
            taskRepository.saveAll(project.getTasks());
        } else if (projectEditDTO.getProjectReviewENUM() == ProjectReviewENUM.EMPTY){
            project.getTasks().forEach(task -> task.setRevisable(false));
            taskRepository.saveAll(project.getTasks());
        }

        return projectRepository.save(project);
    }

    public List<ProjectSearchDTO> findAllByUserAndQuery(Long userId, String query) {
        return userTeamService.findAllByUser(userId)
                .stream()
                .map(UserTeam::getTeam)
                .flatMap(ut -> ut.getProjects().stream())
                .filter(ut -> ut.getName().toLowerCase().contains(query.toLowerCase()))
                .map(ProjectSearchDTO::new)
                .toList();
    }



}
