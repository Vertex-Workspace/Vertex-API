package com.vertex.vertex.permission.repository;

import com.vertex.vertex.permission.model.entity.TaskHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<TaskHour,Long> {
}
