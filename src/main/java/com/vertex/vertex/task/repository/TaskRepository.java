package com.vertex.vertex.task.repository;

import com.vertex.vertex.task.model.entity.TaskProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskProperty, Long> {

//    @Query("SELECT TaskProperty FROM Task JOIN TaskProperty ON Task.id = TaskProperty.task.id")
//    List<TaskProperty> find();
}
