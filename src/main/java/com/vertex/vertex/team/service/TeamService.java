package com.vertex.vertex.team.service;

import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.model.exceptions.TeamNotFoundException;
import com.vertex.vertex.team.relations.group.model.DTO.GroupEditUserDTO;
import com.vertex.vertex.team.relations.group.model.DTO.GroupRegisterDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.model.exception.GroupNameInvalidException;
import com.vertex.vertex.team.relations.group.model.exception.GroupNotFoundException;
import com.vertex.vertex.team.relations.group.service.GroupService;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.permission.service.PermissionService;
import com.vertex.vertex.team.relations.user_team.model.DTO.UserTeamAssociateDTO;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    //Services
    private final UserService userService;
    private final UserTeamService userTeamService;
    private final GroupService groupService;
    private final PermissionService permissionService;

    public void save(TeamViewListDTO teamViewListDTO) {
        try {
            Team team = new Team();
            if (teamViewListDTO.getId() != null) {
                //The Team class has many relations, because of that, when we edit the object, we have to edit
                //just the necessary things in a hard way, as setName...
                team = findTeamById(teamViewListDTO.getId());
            } else {
                team.setName(teamViewListDTO.getName());
                team.setDescription(teamViewListDTO.getDescription());
                //After the Romas explanation about Date
//            team.setCreationDate();
                teamRepository.save(team);
                if(teamViewListDTO.getId() == null) {
                    UserTeamAssociateDTO userTeamAssociateDTO = new UserTeamAssociateDTO();
                    userTeamAssociateDTO.setTeam(team);
                    userTeamAssociateDTO.setUser(teamViewListDTO.getCreator());
                    userTeamAssociateDTO.setCreator(true);
                    editUserTeam(userTeamAssociateDTO);
                }
            }
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

    public void deleteById(Long id) {
        try {
            teamRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Team editGroup(GroupRegisterDTO groupRegisterDTO) {
        List<UserTeam> userTeams = new ArrayList<>();
        try {
            Group group = new Group();

            Team team = findTeamById(groupRegisterDTO.getTeam().getId());

            if (groupRegisterDTO.getName().length() < 1) {
                throw new GroupNameInvalidException();
            }

            group.setName(groupRegisterDTO.getName());
            team.getGroups().add(group);
            group.setTeam(team);

            for (int i = 0; i < groupRegisterDTO.getUsers().size(); i++) {
                User user = userService.findById(groupRegisterDTO.getUsers().get(i).getId());

                for (UserTeam userTeam : team.getUserTeams()) {
                    if (userTeam.getUser().equals(user)) {
                        userTeams.add(userTeam);
                        userTeam.getGroups().add(group);
                        group.setUserTeams(userTeams);
                    }
                }
            }

            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

            User user = userService.findById(userTeam.getUser().getId());
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
                if(!userRemoved) {
                    team.getUserTeams().add(newUserTeam);
                    permissionService.save(user.getId(), team.getId());
                }
            }
            return teamRepository.save(team);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            return findUserInTeam(team, userId);
        }
        return false;
    }

    public Boolean findUserInTeam(Team team, Long userId) {
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

    public void addUsers(TeamInfoDTO dto, Team team) {
        List<User> users = new ArrayList<>();

        team.getUserTeams()
                .forEach(ut -> {
                    users.add(ut.getUser());
                });

        dto.setUsers(users);
    }

    public List<User> getUsersByTeam(Long teamId) {
        List<User> users = new ArrayList<>();
        Team team = findTeamById(teamId);

        for (int i = 0; i < team.getUserTeams().size(); i++) {
            users.add(team.getUserTeams().get(i).getUser());
        }
        return users;
    }

}
