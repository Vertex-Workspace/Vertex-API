package com.vertex.vertex.permission.service;

import com.vertex.vertex.permission.model.entity.Permission;
import com.vertex.vertex.permission.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission save(Permission permissionUser) {
        return permissionRepository.save(permissionUser);
    }

    public Collection<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {
        return permissionRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

}
