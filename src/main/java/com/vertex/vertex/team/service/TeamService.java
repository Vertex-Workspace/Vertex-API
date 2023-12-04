package com.vertex.vertex.team.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.model.DTO.TeamHomeDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            Team team = findById(groupDTO.getTeam().getId());
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
            Team team = findById(userTeam.getTeam().getId());

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

    public Team findById(Long id) {
        if (teamRepository.existsById(id)) {
            return teamRepository.findById(id).get();
        }
        throw new TeamNotFoundException(id);
    }

    public List<TeamHomeDTO> findAll() {
        List<TeamHomeDTO> teamHomeDTOS = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            TeamHomeDTO teamHomeDTO = new TeamHomeDTO();
            BeanUtils.copyProperties(team, teamHomeDTO);
            teamHomeDTOS.add(teamHomeDTO);
        }
        return teamHomeDTOS;
    }

    public void deleteById(Long id) {
        try {
            Team team = findById(id);
            teamRepository.delete(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(Long id) {
        return teamRepository.existsById(id);
    }

}
