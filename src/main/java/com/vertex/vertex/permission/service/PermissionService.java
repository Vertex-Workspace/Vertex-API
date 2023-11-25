package com.vertex.vertex.permission.service;

import com.vertex.vertex.permission.model.entity.TaskHour;
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

    public TaskHour save(TaskHour permissionUser) {
        return permissionRepository.save(permissionUser);
    }

    public Collection<TaskHour> findAll() {
        return permissionRepository.findAll();
    }

    public TaskHour findById(Long id) {
        return permissionRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

}
