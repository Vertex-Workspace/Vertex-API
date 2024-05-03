package com.vertex.vertex.project.service;

import com.vertex.vertex.file.service.FileService;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.service.NotificationService;
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
import com.vertex.vertex.security.ValidationUtils;
import com.vertex.vertex.task.model.DTO.TaskModeViewDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.value.service.ValueService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ProjectViewListDTO saveWithRelationOfProject(ProjectCreateDTO projectCreateDTO, Long teamId) {
        Project project = new Project();
        mapper.map(projectCreateDTO, project);

        createUserTeamAndSetCreator(teamId, project);
        Project savedProject = save(project);
        setCollaboratorsAndVerifyCreator(projectCreateDTO.getUsers(), savedProject);
        project.setProjectReviewENUM(projectCreateDTO.getProjectReviewENUM());
        if(projectCreateDTO.getProjectReviewENUM() == null){
            project.setProjectReviewENUM(ProjectReviewENUM.OPTIONAL);
        }
        defaultPropertyList(project);

        project.setProjectDependency(projectCreateDTO.getProjectDependency());
        return new ProjectViewListDTO(save(project));

    }

    public void createUserTeamAndSetCreator(Long teamId, Project project){
        UserTeam userTeam = userTeamService
                .findUserTeamByComposeId(
                        teamId, project.getCreator().getUser().getId());
        project.setTeam(userTeam.getTeam());
        project.setCreator(userTeam);
        List<UserTeam> userTeams = new ArrayList<>();
        userTeams.add(userTeam);
        project.setCollaborators(userTeams);
    }

    public void setCollaboratorsAndVerifyCreator(List<User> users, Project project) {
        if (users != null) {
            for (User user : users) {
                UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), user.getId());
                if (!project.getCollaborators().contains(userTeam1) && !project.getCreator().equals(userTeam1)) {
                    project.getCollaborators().add(userTeam1);
                    notificationOfCollaborators(userTeam1, project);
                }
            }
        }
    }

    public void notificationOfCollaborators(UserTeam userTeam, Project project) {
        if (userTeam.getUser().getResponsibleInProjectOrTask()) {
            notificationService.save(new Notification(
                    project,
                    "Você foi adicionado(a) como responsável do projeto " + project.getName(),
                    "projeto/" + project.getId() + "/tarefas",
                    userTeam.getUser()
            ));
        }
    }


    public Set<Project> findAllByTeam(Long teamId) {
        return projectRepository.findAllByTeam_Id(teamId);
    }

    public Project updateImage(MultipartFile file, Long projectId) throws IOException {
        Project project = findById(projectId);
        ValidationUtils.loggedUserIsOnProjectAndIsCreator(project);
        project.setFile(fileService.save(file));
        return save(project);
    }

    public Boolean existsByIdAndUserBelongs(Long projectId) {
        if (projectRepository.existsById(projectId)) {
            Project project = findById(projectId);
            ValidationUtils.loggedUserIsOnProject(project);
        }
        return true;
    }

    public Project findById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()){
            return optionalProject.get();
        }
        throw new RuntimeException("Projeto não encontrado!");
    }

    public ProjectOneDTO findProjectById(Long id) {
        Project project = findById(id);

        ProjectOneDTO projectOneDTO = new ProjectOneDTO(project);
        ValidationUtils.loggedUserIsOnProject(project);

        projectOneDTO.setIdTeam(project.getTeam().getId());

        //Pass through all tasks of the project and validates if task has an opened review (UNDERANALYSIS)
        //If it has, It won't be included into list

        projectOneDTO.setTasks(
                getTasksProjectByResponsibility(project.getTasks(),
                        userTeamService.findUserTeamByComposeId(project.getTeam().getId()
                                , ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()))
        );

        return projectOneDTO;
    }

    private List<TaskModeViewDTO> getTasksProjectByResponsibility(List<Task> tasks, UserTeam userTeam){
        return tasks
                .stream()
                .flatMap(task -> task.getTaskResponsables().stream())
                .filter(tr -> tr.getUserTeam().equals(userTeam))
                .map(TaskResponsable::getTask)
//                .flatMap(task -> task.getReviews().stream())
//                .filter(review -> !review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS))
//                .map(Review::getTask)
                .map(TaskModeViewDTO::new)
                .toList();
    }

    public void deleteById(Long id) {
        Project project = findById(id);
        //Delete every value of tasks on project
        project.getTasks().forEach(task -> task.getValues().forEach(valueService::delete));
        ValidationUtils.loggedUserIsOnProjectAndIsCreator(project);

        if (project.getProjectDependency() != null) {
            project.setProjectDependency(null);
            save(project);
        }

        for (Project projectFor : projectRepository.findAll()) {
            if (projectFor.getProjectDependency() != null) {
                if (projectFor.getProjectDependency().getId().equals(id)) {
                    projectFor.setProjectDependency(null);
                    save(projectFor);
                }
            }
        }

        projectRepository.deleteById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public List<PropertyList> defaultStatus(Property property) {
        List<PropertyList> propertiesList = new ArrayList<>();
        propertiesList.add(new PropertyList("Não Iniciado", Color.RED, property, PropertyListKind.TODO, true));
        propertiesList.add(new PropertyList("Em Andamento", Color.YELLOW, property, PropertyListKind.DOING, true));
        propertiesList.add(new PropertyList("Pausado", Color.BLUE, property, PropertyListKind.DOING, false));
        propertiesList.add(new PropertyList("Concluído", Color.GREEN, property, PropertyListKind.DONE, true));
        return propertiesList;
    }

    public List<Property> defaultProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(PropertyKind.STATUS, "Status", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.DATE, "Data", true, null, PropertyStatus.FIXED));
        properties.add(new Property(PropertyKind.LIST, "Dificuldade", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.NUMBER, "Número", false, null, PropertyStatus.VISIBLE));
        properties.add(new Property(PropertyKind.TEXT, "Palavra-Chave", false, null, PropertyStatus.INVISIBLE));
        return properties;
    }

    public void defaultPropertyList(Project project) {
        for (Property property : defaultProperties()) {
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

    public Project updateProjectCollaborators(ProjectEditDTO projectEditDTO) {
        Project project = findById(projectEditDTO.getId());
        ValidationUtils.loggedUserIsOnProjectAndIsCreator(project);

        if(project.getProjectDependency() == null && projectEditDTO.getProjectDependency() != null){
            project.setProjectDependency(projectEditDTO.getProjectDependency());
        }

        project.setName(projectEditDTO.getName());
        project.setDescription(projectEditDTO.getDescription());


        List<UserTeam> userTeamsToAdd = new ArrayList<>();

        for (User user : projectEditDTO.getUsers()) {
            UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(project.getTeam().getId(), user.getId());
            userTeamsToAdd.add(userTeam1);
            if(!project.getCollaborators().contains(userTeam1)){
                notificationOfUpdateCollaborators(userTeam1, project, true);
            }
        }

        if (!userTeamsToAdd.contains(project.getCreator())) {
            userTeamsToAdd.add(project.getCreator());
        }

        //Pass through all removed cola
        for (UserTeam ut : project.getCollaborators()){
            if(!userTeamsToAdd.contains(ut)){
                notificationOfUpdateCollaborators(ut, project, false);
            }
        }
        project.setCollaborators(userTeamsToAdd);
        project.setGroups(projectEditDTO.getGroups());

        return projectRepository.save(project);
    }

    public void notificationOfUpdateCollaborators(UserTeam userTeam, Project project, Boolean bool) {
        String title = bool ? "Agora você é responsável do projeto " : "Você não é mais responsável do projeto ";
        notificationService.save(new Notification(
                project,
                title + project.getName(),
                bool ? "projeto/" + project.getId() + "/tarefas" : "",
                userTeam.getUser()
        ));
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

    public ProjectCollaborators returnAllCollaborators(Long projectId) {
        Project project = findById(projectId);
        return new ProjectCollaborators(
                project.getCollaborators().stream().map(UserTeam::getUser).toList(), project.getGroups());
    }

    /**
     @apiNote Return every project DTO, it will increase the performance of front-end
     the old is returning arround 10MB, now it's 2MB
     */
    public List<ProjectViewListDTO> getAllByTeamAndCollaborators(Long teamId, Long userId) {
        UserTeam userTeam = userTeamService.findUserTeamByComposeId(teamId, userId);
        ValidationUtils.validateUserLogged(userTeam.getUser().getEmail());
        return userTeam.getTeam().getProjects()
                .stream()
                .filter(p -> p.getCollaborators().contains(userTeam))
                .map(ProjectViewListDTO::new)
                .toList();
    }

}
