package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.notification.entity.service.NotificationService;
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
import org.springframework.stereotype.Service;


import javax.swing.text.html.Option;
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

    public UserTeam findUserTeamByComposeId(Long teamId, Long userId) {
        Optional<UserTeam> userTeam = userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId);
        if (userTeam.isPresent()) {
            return userTeam.get();
        }
        throw new RuntimeException("User Team Not Found!");
    }

    public List<TeamViewListDTO> findTeamsByUser(Long userID) {
        List<TeamViewListDTO> teams = new ArrayList<>();
        for (UserTeam userTeam : userTeamRepository.findAllByUser_Id(userID)) {
            TeamViewListDTO dto = new TeamViewListDTO();
            Team team = userTeam.getTeam();
            BeanUtils.copyProperties(team, dto);
            teams.add(dto);
        }
        return teams;
    }

    public List<UserTeam> findAllByTeam(Long teamId) {
        return userTeamRepository.findAllByTeam_Id(teamId);
    }

    public UserTeam findById(Long userTeamId){
        Optional<UserTeam> ut = userTeamRepository.findById(userTeamId);

        if (ut.isPresent()) {
            return ut.get();
        }

        throw new RuntimeException("Não existe um usuário com esse ID!");
    }

    public Team delete(Long teamID, Long userID) {
        UserTeam userTeam = findUserTeamByComposeId(teamID, userID);
        if (userTeam.getUser().getNewMembersAndGroups()) {
            notificationService.groupAndTeam("Você foi removido(a) de " + userTeam.getTeam().getName(), userTeam);
        }
        removeUserTeamDependencies(userTeam);
        return teamRepository.save(userTeam.getTeam());
    }

    public void removeUserTeamDependencies(UserTeam userTeam){
        for (Group group : userTeam.getTeam().getGroups()){
            if(userTeam.getGroups().contains(group)){
                group.getUserTeams().remove(userTeam);
            }
        }
        Team team = userTeam.getTeam();
        team.getUserTeams().remove(userTeam);
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

    public List<UserTeam> findAllByUser(Long userId) {
        return userTeamRepository
                .findAllByUser_Id(userId);
    }


    public List<Permission> getAllPermissionOfAUserTeam(Long userId, Long teamId) {
        return findUserTeamByComposeId(teamId, userId).getPermissionUser();
    }

    public Team saveNewUserTeam(UserTeamAssociateDTO userTeam) {
        try {
            //Find User and Team
            User user = userRepository.findById(userTeam.getUser().getId()).get();
            Team team = teamRepository.findById(userTeam.getTeam().getId()).get();

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
