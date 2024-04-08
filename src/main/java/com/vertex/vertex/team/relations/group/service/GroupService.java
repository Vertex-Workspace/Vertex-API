package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.DTO.AddUsersDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.model.exception.GroupNameInvalidException;
import com.vertex.vertex.team.relations.group.repository.GroupRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final TeamService teamService;
    private final UserTeamService userTeamService;
    private final NotificationService notificationService;

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId).get();
    }


    public Team saveGroup(GroupRegisterDTO groupRegisterDTO) {

        List<UserTeam> userTeams = new ArrayList<>();
        Group group = new Group();

        Team team = teamService.findTeamById(groupRegisterDTO.getTeam().getId());

        if (groupRegisterDTO.getName().isEmpty()) {
            throw new GroupNameInvalidException();
        }

        group.setName(groupRegisterDTO.getName());
        team.getGroups().add(group);
        group.setTeam(team);

        for (User userFor : groupRegisterDTO.getUsers()) {
            for (UserTeam userTeam : team.getUserTeams()) {
                if (userTeam.getUser().equals(userFor)) {
                    userTeams.add(userTeam);
                    userTeam.getGroups().add(group);
                    group.setUserTeams(userTeams);

                    if (userTeam.getUser().getNewMembersAndGroups()) {
                        notificationService.groupAndTeam("Você foi adicionado(a) ao grupo " + group.getName(), userTeam);
                    }
                }
            }
        }
        return teamService.save(team);
    }

    public void delete(Long id) {
        groupRepository.delete(findById(id));
    }

    public void deleteUserFromGroup(Long userId, Long teamId, Long groupId) {
        Group group = findById(groupId);
        for (UserTeam userTeam : group.getUserTeams()) {
            if ((userTeam.getUser().getId().equals(userId)) && (userTeam.getTeam().getId().equals(teamId))) {
                if(userTeam.getUser().getNewMembersAndGroups()){
                    notificationService.groupAndTeam("Você foi removido(a) do grupo " + group.getName(), userTeam);
                }
                userTeam.getGroups().remove(group);
                userTeamService.save(userTeam);
            }
        }
    }

    public List<User> participantsOutOfGroup(Long teamId, Long groupId) {
        List<User> users = new ArrayList<>();
        Team team = teamService.findTeamById(teamId);
        Group group = findById(groupId);
        for (UserTeam userTeam : team.getUserTeams()) {
            if (!group.getUserTeams().contains(userTeam)) {
                users.add(userTeam.getUser());
            }
        }
        return users;
    }

    public void addParticipants(Long id, AddUsersDTO addUsersDTO) {
        Group group = findById(id);
        Team team = group.getTeam();
        for (User user : addUsersDTO.getUsers()) {
            for (UserTeam userTeam : team.getUserTeams()) {
                if (Objects.equals(userTeam.getUser().getId(), user.getId())) {
                    if (!userTeam.getGroups().contains(group)){
                        group.getUserTeams().add(userTeam);
                        userTeam.getGroups().add(group);
                        groupRepository.save(group);
                        userTeamService.save(userTeam);
                        if(userTeam.getUser().getNewMembersAndGroups()){
                            notificationService.groupAndTeam("Você foi adicionado(a) ao grupo " + group.getName(), userTeam);
                        }
                    }
                }
            }
        }
    }
}
