package com.vertex.vertex.team.relations.permission.service;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.entity.service.NotificationService;
import com.vertex.vertex.project.repository.ProjectRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.permission.repository.PermissionRepository;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import com.vertex.vertex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;

    public void save(UserTeam userTeam) {
        try {
            userTeam.setPermissionUser
                    (new Permission()
                            .createBasicPermissions(userTeam, userTeam.getTeam().getCreator().equals(userTeam)));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void changeEnabled(Long permissionId){
        Permission permission = findById(permissionId);

        permission.setEnabled(!permission.isEnabled());
        permissionRepository.save(permission);

        //Notification
        if(permission.getUserTeam().getUser().getPermissionsChanged()){
            String permissionString = permission.isEnabled() ? "Agora você pode " : "Você não pode mais ";
            notificationService.save(new Notification(
                    permission.getUserTeam().getTeam(),
                    permissionString + permission.getName(),
                    "team/" + permission.getUserTeam().getTeam().getId(),
                    permission.getUserTeam().getUser()
            ));
        }
    }


    public Permission findById(Long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        throw new RuntimeException("Permissão não encontrada");
    }


}
