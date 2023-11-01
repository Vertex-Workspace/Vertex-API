package com.vertex.vertex.permission.service;

import com.vertex.vertex.permission.model.entity.PermissionUser;
import com.vertex.vertex.permission.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@AllArgsConstructor
@Service
public class PermissionService {

    private PermissionRepository permissionRepository;

    public PermissionUser save(PermissionUser permissionUser) {
        return permissionRepository.save(permissionUser);
    }

    public Collection<PermissionUser> findAll() {
        return permissionRepository.findAll();
    }

    public PermissionUser findById(Long id) {
        return permissionRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

}
