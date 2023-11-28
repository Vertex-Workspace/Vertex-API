package com.vertex.vertex.task_property.repository;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task_property.model.entity.TaskProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPropertyRepository extends JpaRepository<TaskProperty,Long> {

}
