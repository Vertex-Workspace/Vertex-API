package com.vertex.vertex.permission.service;

import com.vertex.vertex.permission.model.entity.Permission;
import com.vertex.vertex.permission.repository.PermissionRepository;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import com.vertex.vertex.user_team.service.UserTeamService;
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

    public Permission save(Permission permissionUser, Long userTeamId) {
        if (userTeamService.existsById(userTeamId)) {
            UserTeam ut = userTeamService.findById(userTeamId);
            ut.getPermissionUser().add(permissionUser);
            permissionUser.setUserTeam(ut);

            return permissionRepository.save(permissionUser);
        }
        throw new EntityNotFoundException();
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

}
