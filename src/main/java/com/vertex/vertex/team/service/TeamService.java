package com.vertex.vertex.team.service;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //Services
    private final UserService userService;

    private final GroupService groupService;

    public Team save(Team team) {
        try {
            //Create a new row at table User_Team based on the user that has been created the team
            UserTeam userTeam = new UserTeam(userService.findById(team.getCreator().getId()), team);
            team.setUserTeams(List.of(userTeam));
            team.setCreator(userTeam);
            System.out.println(team);

            //After the Romas explanation about Date
//            team.setCreationDate();
            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Team editGroup(GroupDTO groupDTO) {
        try {
            Group group = new Group();
            Team team = teamRepository.findById(groupDTO.getTeam().getId()).get();
            group.setName(groupDTO.getName());
            group.setTeam(team);

            //It verifies if the new group is a subgroup
            if (groupDTO.getGroup()!= null){
                System.out.println("Possui Pai");
                Group fatherGroup = groupService.isSubGroup(team.getId(), groupDTO.getGroup().getId());
                team.getGroups().get(team.getGroups().indexOf(fatherGroup)).getGroups().add(group);
            } else{
                System.out.println("Não Possui Pai");
                group.setGroups(new ArrayList<>());
                team.getGroups().add(group);
            }
            System.out.println(group);
            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Team editUserTeam(UserTeamAssociateDTO userTeam) {
        try {

            User user = userService.findById(userTeam.getUser().getId());
            Team team = teamRepository.findById(userTeam.getTeam().getId()).get();

            boolean userRemoved = false;
            for (UserTeam userTeamFor : team.getUserTeams()) {
                if (userTeamFor.getUser().equals(user)) {
                    team.getUserTeams().remove(userTeamFor);
                    userRemoved = true;
                    break;
                }
            }
            if (!userRemoved) {
                team.getUserTeams().add(new UserTeam(user, team));
            }
            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TeamInfoDTO findById(Long id) {
        TeamInfoDTO dto = new TeamInfoDTO(); //retorna as informações necessárias para a tela de equipe
        Team team;

        if (teamRepository.existsById(id)) {
            team = teamRepository.findById(id).get();
           BeanUtils.copyProperties(team, dto);
           addUsers(dto, team); //adiciona os usuários ao grupo com base no userTeam, para utilização no fe
           return dto;
        }
        throw new TeamNotFoundException(id);
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

    public void deleteById(Long id) {
        try {
            if (teamRepository.existsById(id)) {
                Team team = teamRepository.findById(id).get();
                teamRepository.delete(team);
            }
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
            return findUserInTeam(team, userId);
        }
        return false;
    }

    public Boolean findUserInTeam(Team team, Long userId) {
        return team.getUserTeams().stream()
                .anyMatch(ut -> Objects.equals(ut.getUser().getId(), userId));
    }


    public Team findTeamById(Long id) {
        if (teamRepository.existsById(id)) {
            return teamRepository.findById(id).get();
        }
        throw new EntityNotFoundException();
    }

    public void addUsers(TeamInfoDTO dto, Team team) {
        List<User> users = new ArrayList<>();

        team.getUserTeams()
                .forEach(ut -> {
                    users.add(ut.getUser());
                });

        dto.setUsers(users);
    }

}
