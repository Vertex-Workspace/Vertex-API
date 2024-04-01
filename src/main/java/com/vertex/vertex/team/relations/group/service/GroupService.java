package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.repository.GroupRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;
    private final UserTeamService userTeamService;

    public Group edit(Group group) {
        return groupRepository.save(group);
    }

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId).get();
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public void delete(Long id) {
        Group group = findById(id);

        for (UserTeam userTeam : group.getUserTeams()) {
            userTeam.getGroups().remove(group);
            System.out.println(userTeam.getGroups());
        }

        Team team = group.getTeam();
        team.getGroups().remove(findById(id));
        teamRepository.save(team);
        groupRepository.delete(group);
    }

    public void deleteUserFromGroup(Long userId, Long teamId, Long groupId) {
        Group group = findById(groupId);
        for (UserTeam userTeam : group.getUserTeams()) {
            if ((userTeam.getUser().getId().equals(userId)) && (userTeam.getTeam().getId().equals(teamId))) {
                userTeam.getGroups().remove(group);
                userTeamService.save(userTeam);
            }
        }
    }

    public List<User> participantsOutOfGroup(Long teamId, Long groupId) {
        List<User> users = new ArrayList<>();
        Team team = teamRepository.findById(teamId).get();
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
                    }
                }
            }
    }

    public Group editGroupName(Group group, Long teamId){
        Team team = teamRepository.findById(teamId).get();
        group.setTeam(team);
        return groupRepository.save(group);
    }
}
