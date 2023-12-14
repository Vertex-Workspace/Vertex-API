package com.vertex.vertex.task.relations.task_hours.repository;

import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHoursRepository extends JpaRepository<TaskHour, Long> {




}
