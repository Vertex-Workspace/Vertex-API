package com.vertex.vertex.task.repository;

import com.vertex.vertex.task.model.entity.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByGoogleId(String googleId);

    Optional<Task> findByGoogleId(String id);


//    @Query("SELECT TaskProperty FROM com.vertex.vertex.task.controller.Task JOIN TaskProperty ON com.vertex.vertex.task.controller.Task.id = TaskProperty.task.id")
//    List<TaskProperty> find();


}
