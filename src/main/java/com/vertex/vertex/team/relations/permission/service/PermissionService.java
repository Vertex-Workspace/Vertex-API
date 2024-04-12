package com.vertex.vertex.team.relations.permission.service;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.permission.repository.PermissionRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserTeamService userTeamService;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;

    public void save(Long userId, Long teamId) {
        List<Permission> permissions;
        UserTeam userTeam = getOneUserByTeam(userId,
                teamId);

        Permission permission2 = new Permission();
        permission2.setName(TypePermissions.Criar);
        Permission permission3 = new Permission();
        permission3.setName(TypePermissions.Deletar);
        Permission permission4 = new Permission();
        permission4.setName(TypePermissions.Editar);

        if(userTeam.getTeam().getCreator().equals(userTeam)){
            permission2.setEnabled(true);
            permission3.setEnabled(true);
            permission4.setEnabled(true);
        }else {
            permission2.setEnabled(false);
            permission3.setEnabled(false);
            permission4.setEnabled(false);
        }


        permissions = List.of(permission2, permission3, permission4);

        userTeam.setPermissionUser(permissions);
        for (Permission permission : permissions) {
            permission.setUserTeam(userTeam);
        }
    }

    public void changeEnabled(Long permissionId, Long userId, Long teamId){
        UserTeam userTeam = getOneUserByTeam(userId, teamId);
        for(Permission permission : userTeam.getPermissionUser()){
            if(permissionId.equals(permission.getId())){
                permission.setEnabled(!permission.isEnabled());
                permissionRepository.save(permission);
                //Notificaiton
                if(userTeam.getUser().getPermissionsChanged()){
                    String permissionString = permission.isEnabled() ? "Agora você pode " : "Você não pode mais ";
                    notificationService.save(new Notification(
                            userTeam.getTeam(),
                            permissionString + permission.getName(),
                            "team/" + teamId,
                            userTeam.getUser()
                    ));
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

}
