package com.vertex.vertex.task.service;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

}
