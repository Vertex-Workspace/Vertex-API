package com.vertex.vertex.task.relations.task_responsables.repository;

import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskResponsablesRepository extends JpaRepository<TaskResponsable ,Long> {



}
