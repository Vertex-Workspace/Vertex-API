package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.security.util.ValidationUtils;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserTeamService {

    //Repositories
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    //Services
    private final PermissionService permissionService;
    private final NotificationService notificationService;

    public UserTeam save(UserTeam userTeam) {
        return userTeamRepository.save(userTeam);
    }

    public UserTeam findUserTeamByComposeId(Long teamId, Long userID) {
        Optional<UserTeam> userTeam = userTeamRepository.findByTeam_IdAndUser_Id(teamId, userID);
        if (userTeam.isPresent()) {
            ValidationUtils.validateUserLogged(userTeam.get().getUser().getEmail());
            return userTeam.get();
        }
        throw new RuntimeException("User Team Not Found!");
    }

    public List<Team> findTeamsByUserId(Long userID){
        return userTeamRepository.findAllByUser_Id(userID)
                .stream()
                .map(UserTeam::getTeam)
                .toList();
    }


    public List<TeamViewListDTO> findTeamsByUser(Long userID) {
        ValidationUtils.validateUserLogged(userRepository.findById(userID).get().getEmail());

        List<TeamViewListDTO> teams = new ArrayList<>();
        for (UserTeam userTeam : userTeamRepository.findAllByUser_Id(userID)) {
            TeamViewListDTO dto = new TeamViewListDTO();
            Team team = userTeam.getTeam();
            BeanUtils.copyProperties(team, dto);

            dto.setIsCreator(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getId()
                    .equals(team.getCreator().getUser().getId()));

            dto.setProjects(team.getProjects().stream().map(ProjectViewListDTO::new).toList());
            teams.add(dto);
        }
        return teams;
    }

    public List<UserTeam> findAllByTeam(Long teamId) {
        return userTeamRepository.findAllByTeam_Id(teamId);
    }

    public UserTeam findById(Long userTeamId) {
        Optional<UserTeam> userTeamOptional = userTeamRepository.findById(userTeamId);
        if (userTeamOptional.isPresent()) {
            return userTeamOptional.get();
        }
        throw new RuntimeException("User team não encontrado!");
    }

    public void delete(Long teamID, Long userID) {
        UserTeam userTeam = findUserTeamByComposeId(teamID, userID);
        ValidationUtils.validateUserLogged(userTeam.getUser().getEmail());
        if (userTeam.getUser().getNewMembersAndGroups()) {
            notificationService.groupAndTeam("Você foi removido(a) de " + userTeam.getTeam().getName(), userTeam);
        }
        Team team = userTeam.getTeam();
        team.getUserTeams().remove(userTeam);
        userTeam.getChats().forEach(chat -> chat.getUserTeams().remove(userTeam));
        teamRepository.save(team);
        userTeamRepository.delete(userTeam);

    }

    public void removeUserTeamDependencies(UserTeam userTeam){
        for (Group group : userTeam.getTeam().getGroups()){
            if(userTeam.getGroups().contains(group)){
                group.getUserTeams().remove(userTeam);
            }
        }
        Team team = userTeam.getTeam();
        team.getUserTeams().remove(userTeam);
        if(team.getCreator().equals(userTeam)){
            team.setCreator(null);
        }
        team.getChat().getUserTeams().remove(userTeam);
        userTeam.getChats().forEach(chat -> chat.getUserTeams().remove(userTeam));
    }

    public List<UserTeam> findAllByUserAndQuery(Long userId, String query) {
        return userTeamRepository
                .findAllByUser_IdAndTeam_NameContainingIgnoreCase(userId, query);
    }

    public List<UserTeam> findAllUserTeamByUserId(Long userId) {
        return userTeamRepository
                .findAllByUser_Id(userId);
    }



    public List<Permission> getAllPermissionOfAUserTeam(Long teamId, Long userId) {
        return userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId).get().getPermissionUser();
    }

    public Team saveNewUserTeam(UserTeamAssociateDTO userTeam) {
        try {
            //Find User and Team
            User user = userRepository.findById(userTeam.getUser().getId()).get();
            Team team = teamRepository.findById(userTeam.getTeam().getId()).get();

            ValidationUtils.validateUserLogged(user.getEmail());

            //Save new User Team
            UserTeam savedUserTeam = save(new UserTeam(user, team));

            team.getUserTeams().add(savedUserTeam);
//
////            Set the Creator
            if (team.getCreator() == null && userTeam.isCreator()) {
                team.setCreator(savedUserTeam);
            }

//            //Permissions Default
            teamRepository.save(team);
            permissionService.save(savedUserTeam);
            team.getChat().getUserTeams().add(savedUserTeam);

            //Notifications
            //To the new user
            if (savedUserTeam.getUser().getNewMembersAndGroups()) {
                notificationService.groupAndTeam("Você entrou em " + team.getName(), savedUserTeam);
            }
            //To all users on team
            for (UserTeam userTeamFor : team.getUserTeams()) {
                if (!userTeamFor.equals(savedUserTeam) && userTeamFor.getUser().getNewMembersAndGroups()) {
                    notificationService.groupAndTeam(savedUserTeam.getUser().getFullName() + " entrou em " + team.getName(), userTeamFor);
                }
            }

            return team;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
