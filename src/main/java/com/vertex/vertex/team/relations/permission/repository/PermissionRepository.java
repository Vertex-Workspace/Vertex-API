package com.vertex.vertex.team.relations.permission.repository;

import com.vertex.vertex.team.relations.permission.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
