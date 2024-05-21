package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.model.exception.GroupNameInvalidException;
import com.vertex.vertex.team.relations.group.repository.GroupRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final TeamService teamService;
    private final UserTeamService userTeamService;
    private final NotificationService notificationService;


    public Group findById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isPresent()){
            return optionalGroup.get();
        }
        throw new RuntimeException("Grupo não encontrado!");
    }


    public void delete(Long id) {
        Group group = findById(id);
        group.setTeam(null);
        groupRepository.save(group);
        groupRepository.delete(group);
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

    public void addParticipants(Long groupId, Long userId) {
        Group group = findById(groupId);
        Team team = group.getTeam();
        for (UserTeam userTeam : team.getUserTeams()) {
            if (userTeam.getUser().getId().equals(userId)) {
                if (userTeam.getGroups().contains(group)){
                    throw new RuntimeException("Esse userTeam já está no grupo");
                } else {
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

    public Group editGroupName(Group group, Long teamId) {
        Team team = teamService.findTeamById(teamId);
        group.setTeam(team);
        return groupRepository.save(group);
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
}
