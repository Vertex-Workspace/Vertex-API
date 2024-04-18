package com.vertex.vertex.team.service;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.log.model.exception.EntityDoesntExistException;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.review.service.ReviewService;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamLinkDTO;
import com.vertex.vertex.team.model.DTO.TeamSearchDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupEditUserDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.model.exception.GroupNameInvalidException;
import com.vertex.vertex.team.relations.group.model.exception.GroupNotFoundException;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.utils.PerformanceUtils;
import com.vertex.vertex.utils.RandomCodeUtils;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //Services
    private final TaskService taskService;
    private final UserTeamService userTeamService;
    private final ProjectService projectService;
    private final ReviewService reviewService;
    private final PermissionService permissionService;
    //Model Mapper
    private final ModelMapper mapper;

    //Utils
    private final PerformanceUtils performanceUtils;

    public Team save(TeamViewListDTO teamViewListDTO) {
        try {
            Team team = new Team();
            if (teamViewListDTO.getId() != null) {
                //The Team class has many relations, because of that, when we edit the object, we have to edit
                //just the necessary things in a hard way, as setName...
                team = findTeamById(teamViewListDTO.getId());
            }
            //name and description
            mapper.map(teamViewListDTO, team);

                team.setInvitationCode(RandomCodeUtils.generateInvitationCode());

            UserTeam userTeam = new UserTeam(teamViewListDTO.getCreator(), team);

            team.setCreator(userTeam);
            team.getUserTeams().add(userTeam);
            team.setChat(new Chat(team));

            teamRepository.save(team);

            if(teamViewListDTO.isDefaultTeam()){
                saveDefaultTasksAndProject(team);
            }

            //this part isn't working. check
            permissionService.save(userTeam);

            return teamRepository.save(team);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public TeamInfoDTO findById(Long id) {
        TeamInfoDTO dto = new TeamInfoDTO(); //retorna as informações necessárias para a tela de equipe
        Team team = findTeamById(id);
        mapper.map(team, dto);
        //Convert UserTeam -> User
        dto.setUsers(getUsersByTeam(dto.getId()));
        //Convert Project -> ProjectOneDTO
        dto.setProjects(convertTeamProjectsToDto(team));
        dto.setImage(team.getImage());
        dto.setCreator(team.getCreator().getUser());
        calculatePerformance(dto, team);
        return dto;
    }

    public TeamLinkDTO findInvitationCodeById(Long id) {
        try {
            return new TeamLinkDTO(findTeamById(id).getInvitationCode());
        } catch (Exception e) {
            throw new TeamNotFoundException(id);
        }
    }

    public void deleteById(Long id) {
        try {
            teamRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Group> findGroupsByTeamId(Long idTeam) {
        try {
            return findById(idTeam).getGroups();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(EntityDoesntExistException::new);
    }

    public TeamInfoDTO updateImage(MultipartFile file, Long teamId) {
        try {
            Team team = findTeamById(teamId);
            if (!Objects.requireNonNull(file.getContentType()).contains("image")) {
                throw new RuntimeException("Arquvio deve ser imagem!");
            }
            team.setImage(file.getBytes());
            teamRepository.save(team);
            //Return some aggregates attributes
            return findById(teamId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getUsersByTeamAndGroup(Long teamId) {
        List<User> users = new ArrayList<>();
        List<UserTeam> userGroups = new ArrayList<>();
        Team team = findTeamById(teamId);
        if (!team.getGroups().isEmpty()) {
            List<UserTeam> userTeams = new ArrayList<>(team.getUserTeams());
            for(Group group : team.getGroups()){
                userGroups = group.getUserTeams();
            }
            for(UserTeam userTeam: userTeams){
                if(!userGroups.contains(userTeam)){
                    users.add(userTeam.getUser());
                }
            }
        }else {
            for(UserTeam userTeam : team.getUserTeams()) {
                users.add(userTeam.getUser());
            }
        }
        return users;
    }

    public List<User> getUsersByTeam(Long teamId) {
        return findTeamById(teamId)
                .getUserTeams()
                .stream()
                .map(UserTeam::getUser)
                .toList();
    }


    //Refatorar Depois de finalizar o projeto...
    public void saveDefaultTasksAndProject(Team team) {

        User user = team.getCreator().getUser();

        ProjectCreateDTO projectDefault1 =
                new ProjectCreateDTO("Projeto Pessoal", "Seu projeto pessoal padrão", team.getCreator(), List.of(user));

        Project project = projectService.saveWithRelationOfProject(projectDefault1, team.getId());

        TaskCreateDTO taskCreateDTO1 =
                new TaskCreateDTO("Tarefa 1", "Sua tarefa é lavar a louça", team.getCreator().getUser(), project);
        TaskCreateDTO taskCreateDTO2 =
                new TaskCreateDTO("Tarefa 2", "Sua tarefa é lavar a louça", team.getCreator().getUser(), project);

        taskService.save(taskCreateDTO1);
        taskService.save(taskCreateDTO2);
    }

    public List<ProjectViewListDTO> convertTeamProjectsToDto(Team team) {
        return team.getProjects()
                .stream()
                .map(ProjectViewListDTO::new)
                .toList();
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public List<Task> getAllTasksByTeam(Long id, Long userID) {
        try {
            UserTeam userTeam = userTeamService.findUserTeamByComposeId(id, userID);
            return findTeamById(id)
                    .getProjects()
                    .stream()
                    .flatMap(p -> p.getTasks().stream())
                    .flatMap(t -> t.getTaskResponsables().stream())
                    .filter(tr -> tr.getUserTeam().equals(userTeam))
                    .map(TaskResponsable::getTask)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<TeamSearchDTO> findAllByUserAndQuery(Long userId, String query) {
        return userTeamService
                .findAllByUserAndQuery(userId, query)
                .stream()
                .map(UserTeam::getTeam)
                .map(TeamSearchDTO::new)
                .toList();
    }

    public List<UserTeam> findAllUserTeamsByUserID(Long userId) {
        return userTeamService
                .findAllUserTeamByUserId(userId);
    }


    private void calculatePerformance(TeamInfoDTO dto, Team team) {
        //Tasks Graphics
        dto.setTasksPerformances(performanceUtils.tasksSplitByCategory(team.getProjects()));
        //Percentage
        dto.setPercentage(performanceUtils.calculatePercentage(dto.getTasksPerformances()));
        //Time Table
        dto.setReviewHoursDTOS(performanceUtils.getTimeOnTasks(team));

        //Reviews
        List<Review> reviews = reviewService.getReviewsByProjects(team.getProjects());
        dto.setReprovedReviews(performanceUtils.getReviewsByStatus(reviews, ApproveStatus.DISAPPROVED));
        dto.setApprovedReviews(performanceUtils.getReviewsByStatus(reviews, ApproveStatus.APPROVED));
        //Average of review grades
        dto.setAverageReviews(performanceUtils.calculateAverage(reviews));
    }
}
