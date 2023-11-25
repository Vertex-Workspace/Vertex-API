package com.vertex.vertex.task_hours.repository;

import com.vertex.vertex.task_hours.model.entity.TaskHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHoursRepository extends JpaRepository<TaskHour, Long> {
}
