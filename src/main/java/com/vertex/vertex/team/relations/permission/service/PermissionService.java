package com.vertex.vertex.team.relations.permission.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.permission.model.DTOs.PermissionCreateDTO;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.permission.repository.PermissionRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserTeamService userTeamService;
    private final TeamService teamService;
    private final UserService userService;

    public Permission save(PermissionCreateDTO permissionCreateDTO) {
        Permission permissionUser = new Permission();
        BeanUtils.copyProperties(permissionCreateDTO, permissionUser);
        Team team = teamService.findTeamById(permissionCreateDTO.getTeam().getId());
        User user = userService.findById(permissionCreateDTO.getUserId());

        permissionRepository.save(permissionUser);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permissionUser);
        UserTeam userTeam = getOneUserByTeam(user.getId(),
                team.getId());
        userTeam.setPermissionUser(permissions);
        permissionUser.setUserTeam(userTeam);
        return permissionUser;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {
        if (permissionRepository.existsById(id)) {
            return permissionRepository.findById(id).get();
        }
        throw new EntityNotFoundException();
    }

    public void deleteById(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public UserTeam getOneUserByTeam(Long userId, Long teamId){
        Team team = teamService.findTeamById(teamId);
        for(UserTeam userTeam : team.getUserTeams()){
            if(userTeam.getTeam().getId().equals(teamId) && userTeam.getUser().getId().equals(userId)){
                return userTeam;
            }
        }
        return null;
    }

}
