package com.vertex.vertex.task_property.repository;

import com.vertex.vertex.task_property.model.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPropertyRepository extends JpaRepository<Value,Long> {
}
