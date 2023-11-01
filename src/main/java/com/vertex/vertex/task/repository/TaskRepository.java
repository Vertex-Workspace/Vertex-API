package com.vertex.vertex.task.repository;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task_property.model.entity.TaskProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT TaskProperty FROM Task JOIN TaskProperty ON Task.id = TaskProperty.task.id")
    List<TaskProperty> find();
}
