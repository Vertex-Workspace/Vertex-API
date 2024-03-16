package com.vertex.vertex.team.service;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.project.model.DTO.ProjectCreateDTO;
import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamLinkDTO;
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
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
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
    private final ChatService chatService;
    private final GroupService groupService;
    private final PermissionService permissionService;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    public Team save(TeamViewListDTO teamViewListDTO) {
        try {
            Team team = new Team();
            if (teamViewListDTO.getId() != null) {
                //The Team class has many relations, because of that, when we edit the object, we have to edit
                //just the necessary things in a hard way, as setName...
                team = findTeamById(teamViewListDTO.getId());
            }
            team.setName(teamViewListDTO.getName());
            team.setDescription(teamViewListDTO.getDescription());
 
    
            String invitationCode = generateInvitationCode();
            team.setInvitationCode(invitationCode);
            teamRepository.save(team);
            if (teamViewListDTO.getId() == null) {
                UserTeamAssociateDTO userTeamAssociateDTO = new UserTeamAssociateDTO();
                userTeamAssociateDTO.setTeam(team);
                userTeamAssociateDTO.setUser(teamViewListDTO.getCreator());
                userTeamAssociateDTO.setCreator(true);
                Team teamWithUserTeam = editUserTeam(userTeamAssociateDTO);

                createChatForTeam(teamWithUserTeam);

                if(teamViewListDTO.isDefaultTeam()) {
                    saveDefaultTasksAndProject(teamWithUserTeam);
                }
            }
            return findTeamById(team.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createChatForTeam(Team team) {

        Chat chat = new Chat();

        List<UserTeam> userTeams = userTeamService.findAllByTeam(team.getId());
        chat.setUserTeams(userTeams);
        chat.setName(team.getName());

        Chat chatSaved = chatService.create(chat);

        for (UserTeam userTeam : team.getUserTeams()) {
            if (userTeam.getChats() == null) {
                List<Chat> newChats = new ArrayList<>();
                newChats.add(chatSaved);
                userTeam.setChats(newChats);
                team.setChat(chatSaved);
            } else {
                userTeam.getChats().add(chatSaved);
            }
            userTeamService.save(userTeam);
            teamRepository.save(team);
        }
    }


    private String generateInvitationCode() {
        String caracteres = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < caracteres.length(); i++) {
            char a = caracteres.charAt(random.nextInt(34));
            token.append(a);
        }
        return token.toString();
    }



    public TeamInfoDTO findById(Long id) {
        TeamInfoDTO dto = new TeamInfoDTO(); //retorna as informações necessárias para a tela de equipe
        Team team = findTeamById(id);
        List<ProjectViewListDTO> projectList = convertTeamProjectsToDto(team);
        BeanUtils.copyProperties(team, dto);
        dto.setUsers(getUsersByTeam(dto.getId())); //adiciona os usuários ao grupo com base no userTeam, para utilização no fe
        dto.setProjects(projectList);
        dto.setImage(team.getImage());
        return dto;
    }

    public TeamLinkDTO findInvitationCodeById(Long id) {
        try {
            return new TeamLinkDTO(teamRepository.findById(id).get().getInvitationCode());
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


    public Team editGroup(GroupRegisterDTO groupRegisterDTO) {

        List<UserTeam> userTeams = new ArrayList<>();
        Group group = new Group();

        Team team = findTeamById(groupRegisterDTO.getTeam().getId());

        if (groupRegisterDTO.getName().length() < 1) {
            throw new GroupNameInvalidException();
        }

        group.setName(groupRegisterDTO.getName());
        team.getGroups().add(group);
        group.setTeam(team);

        for (int i = 0; i < groupRegisterDTO.getUsers().size(); i++) {
            for (UserTeam userTeam : team.getUserTeams()) {
                if (userTeam.getUser().equals(groupRegisterDTO.getUsers().get(i))) {
                    userTeams.add(userTeam);
                    userTeam.getGroups().add(group);
                    group.setUserTeams(userTeams);
                }
            }
        }

        return teamRepository.save(team);
    }

    public Group editUserIntoGroup(GroupEditUserDTO groupEditUserDTO) {
        try {
            Group group = groupService.findById(groupEditUserDTO.getGroupId());
            UserTeam userTeam = userTeamService.findById(groupEditUserDTO.getUserTeam().getId());

            if (userTeam.getTeam().getGroups().contains(group)) {
                //This logic makes a validation to verify if the user is already associated with this group
                //If it is, then the associated will be removed.
                if (userTeam.getGroups().contains(group) || group.getUserTeams().contains(userTeam)) {
                    userTeam.getGroups().remove(group);
                    group.getUserTeams().remove(userTeam);
                }
                //Else, it will be associated and the user will be on the group.
                else {
                    userTeam.getGroups().add(group);
                    group.getUserTeams().add(userTeam);
                }
                return groupService.edit(group);
            } else {
                throw new GroupNotFoundException(group.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Team editUserTeam(UserTeamAssociateDTO userTeam) {
        try {
            List<UserTeam> userTeams = new ArrayList<>();

            User user = userRepository.findById(userTeam.getUser().getId()).get();
            Team team = teamRepository.findById(userTeam.getTeam().getId()).get();
            UserTeam newUserTeam = new UserTeam(user, team);

            if (team.getUserTeams() == null) {

                userTeams.add(newUserTeam);
                team.setUserTeams(userTeams);

                if (userTeam.isCreator()) {
                    team.setCreator(newUserTeam);
                }
                //set the default permissions
                permissionService.save(user.getId(), team.getId());
            } else {
                boolean userRemoved = false;
                for (UserTeam userTeamFor : team.getUserTeams()) {
                    if (userTeamFor.getUser().equals(user)) {
                        team.getUserTeams().remove(userTeamFor);
                        userRemoved = true;
                        break;
                    }
                }
                if (!userRemoved) {
                    team.getUserTeams().add(newUserTeam);
                    permissionService.save(user.getId(), team.getId());
                }
            }
            teamRepository.save(team);

            UserTeam userTeam1 = userTeamService.findUserTeamByComposeId(team.getId(), user.getId());

            if (team.getProjects() != null) {
                for (Project project : team.getProjects()) {
                    for (Task task : project.getTasks()) {
                        task.getTaskResponsables().add(new TaskResponsable(userTeam1, task));
                        taskService.save(task);
                    }
                }
            }

            return team;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userIsOnTeam(Long idUser, Long idTeam) {

        User user = userRepository.findById(idUser).get();
        Team team = teamRepository.findById(idTeam).get();

        for (UserTeam userTeamFor : team.getUserTeams()) {
            if (userTeamFor.getUser().equals(user)) {
                return true;
            }
        }
        return false;

    }

    public List<TeamInfoDTO> findAll() {
        List<TeamInfoDTO> teamHomeDTOS = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            TeamInfoDTO dto = new TeamInfoDTO();
            BeanUtils.copyProperties(team, dto);
            teamHomeDTOS.add(dto);
        }
        return teamHomeDTOS;
    }

    public List<Group> findGroupsByTeamId(Long idTeam) {
        try {
            return findById(idTeam).getGroups();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

    public Boolean existsByIdAndUserBelongs(Long teamId, Long userId) {
        if (teamRepository.existsById(teamId)) {
            Team team = findTeamById(teamId);
            return userExistsInTeam(team, userId);
        }
        return false;
    }

    public Boolean userExistsInTeam(Team team, Long userId) {
        return team.getUserTeams().stream()
                .anyMatch(ut ->
                        Objects.equals(ut.getUser().getId(), userId)
                );
    }

    public Team findTeamById(Long id) {
        try {
            return teamRepository.findById(id).get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateImage(MultipartFile file, Long teamId) {
        try {
            Team team = findTeamById(teamId);
            team.setImage(file.getBytes());
            teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getUsersByTeam(Long teamId) {
        return findTeamById(teamId)
                .getUserTeams()
                .stream()
                .map(UserTeam::getUser)
                .toList();
    }

    public void deleteUserTeam(Long teamId, Long userId) {
        Team team = findTeamById(teamId);
        User user = userRepository.findById(userId).get();
        UserTeam userTeam = null;
        for (UserTeam userTeamFor : team.getUserTeams()) {
            if (userTeamFor.getUser().equals(user) && userTeamFor.getTeam().equals(team)) {
                userTeam = userTeamFor;
            }
        }
        if (userTeam != null) {
            team.getUserTeams().remove(userTeam);
            userTeam.setTeam(null);
            teamRepository.save(team);
        }
    }

    public User teamCreatorId(Long teamId) {
        Team team = teamRepository.findById(teamId).get();
        UserTeam userTeam = team.getCreator();
        return userTeam.getUser();
    }

    public void saveDefaultTasksAndProject(Team team){
        File file1  = new File("src/main/java/com/vertex/vertex/upload/casaPadrão.png");
        DiskFileItem fileItem = new DiskFileItem("file", "image/png", false, file1.getName(),
                (int) file1.length(), file1.getParentFile());
        fileItem.getOutputStream();

        File file2  = new File("src/main/java/com/vertex/vertex/upload/profissionalPadrao.jfif");
        DiskFileItem fileItem2 = new DiskFileItem("file", "image/png", false, file2.getName(),
                (int) file2.length(), file2.getParentFile());
        fileItem2.getOutputStream();
        System.out.println(fileItem);

        Project projectDefault1 =
                new Project("Projeto Pessoal", "Seu projeto pessoal padrão", fileItem.get(), team, team.getCreator(), List.of(team.getCreator()));
        Project projectDefault2 =
                new Project("Projeto Profissional", "Seu projeto pessoal padrão", fileItem2.get(), team, team.getCreator(),  List.of(team.getCreator()));

        TaskCreateDTO taskCreateDTO1 =
                new TaskCreateDTO("Lavar a louça", "Sua tarefa é lavar a louça", team.getCreator(), projectDefault1);
        TaskCreateDTO taskCreateDTO2 =
                new TaskCreateDTO("Apresentar seminário", "Sua tarefa é lavar a louça", team.getCreator(), projectDefault2);

        projectService.defaultProperties(projectDefault1);
        projectService.defaultProperties(projectDefault2);

        projectService.save(projectDefault1);
        projectService.save(projectDefault2);

        taskService.save(taskCreateDTO1);
        taskService.save(taskCreateDTO2);
    }

    public List<ProjectViewListDTO> convertTeamProjectsToDto(Team team) {
        return team.getProjects()
                .stream()
                .map(ProjectViewListDTO::new)
                .toList();
    }
    public List<Task> getAllTasksByTeam(Long id) {
        try {
            return findTeamById(id)
                    .getProjects()
                    .stream()
                    .flatMap(p -> p.getTasks().stream())
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
