package com.vertex.vertex.permission.repository;

import com.vertex.vertex.permission.model.entity.PermissionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionUser,Long> {
}
