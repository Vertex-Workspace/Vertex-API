package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.security.ValidationUtils;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
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
        ValidationUtils.validateUserLogged(userRepository.findById(userID).get().getEmail());
        Optional<UserTeam> userTeam = userTeamRepository.findByTeam_IdAndUser_Id(teamId, userID);
        if (userTeam.isPresent()) {
            return userTeam.get();
        }
        throw new RuntimeException("User Team Not Found!");
    }

    public List<TeamViewListDTO> findTeamsByUser(Long userID) {
        ValidationUtils.validateUserLogged(userRepository.findById(userID).get().getEmail());

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
        userTeamRepository.delete(findUserTeamByComposeId(teamID, userID));
    }


    public List<UserTeam> findAllByUserAndQuery(Long userId, String query) {
        return userTeamRepository
                .findAllByUser_IdAndTeam_NameContainingIgnoreCase(userId, query);
    }

    public List<UserTeam> findAllUserTeamByUserId(Long userId) {
        ValidationUtils.validateUserLogged(userRepository.findById(userId).get().getEmail());
        return userTeamRepository
                .findAllByUser_Id(userId);
    }


    public List<UserTeam> findAllByUser(Long userId) {
        ValidationUtils.validateUserLogged(userRepository.findById(userId).get().getEmail());
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
