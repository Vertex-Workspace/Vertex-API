package com.vertex.vertex.team.relations.permission.service;

import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.permission.repository.PermissionRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserTeamService userTeamService;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;

    public void save(Long userId, Long teamId) {
        UserTeam userTeam = getOneUserByTeam(userId,
                teamId);
        Permission permission1 = new Permission();
        permission1.setName(TypePermissions.VIEW);
        permission1.setEnabled(true);

        Permission permission2 = new Permission();
        permission2.setName(TypePermissions.CREATE);
        permission2.setEnabled(false);

        Permission permission3 = new Permission();
        permission3.setName(TypePermissions.DELETE);
        permission3.setEnabled(false);

        Permission permission4 = new Permission();
        permission4.setName(TypePermissions.EDIT);
        permission4.setEnabled(false);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission1);
        permissions.add(permission2);
        permissions.add(permission3);
        permissions.add(permission4);

        userTeam.setPermissionUser(permissions);
        for (Permission permission : permissions) {
            permission.setUserTeam(userTeam);
        }
    }

    public void changeEnabled(Long permissionId, Long userId, Long teamId){
        UserTeam userTeam = getOneUserByTeam(userId, teamId);
        for(Permission permission : userTeam.getPermissionUser()){
            if(permissionId.equals(permission.getId())){
                if(permission.getName() != TypePermissions.VIEW){
                    permission.setEnabled(!permission.isEnabled());
                    permissionRepository.save(permission);
                }
            }
        }
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

    public UserTeam getOneUserByTeam(Long userId, Long teamId) {
        Team team = teamRepository.findById(teamId).get();
        for (UserTeam userTeam : team.getUserTeams()) {
            if (userTeam.getTeam().getId().equals(teamId) && userTeam.getUser().getId().equals(userId)) {
                return userTeam;
            }
        }
        return null;
    }

    public List<Permission> getAllPermissionOfAUserTeam(Long userId, Long teamId) {
        UserTeam userTeam = getOneUserByTeam(userId, teamId);
        return userTeam.getPermissionUser();
    }
    public List<Permission> hasPermission(Long projectId, Long userId){
        Team team = projectRepository.findById(projectId).get().getTeam();
        UserTeam userTeam = getOneUserByTeam(userId, team.getId());
        return userTeam.getPermissionUser();
    }

    public void permissionsPatch(UserTeam userTeam){

    }

}
