package com.vertex.vertex.task.service;

import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskProperty save(TaskProperty task) {
        return taskRepository.save(task);
    }

    public Collection<TaskProperty> findAll() {
        return taskRepository.findAll();
    }

    public TaskProperty findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

}
