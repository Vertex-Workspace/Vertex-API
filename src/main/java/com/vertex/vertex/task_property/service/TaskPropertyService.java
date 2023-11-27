package com.vertex.vertex.task_property.service;


import com.vertex.vertex.task_property.model.entity.TaskProperty;
import com.vertex.vertex.task_property.repository.TaskPropertyRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
@Data
public class TaskPropertyService {
    private final TaskPropertyRepository taskPropertyRepository;

    public TaskProperty save(TaskProperty task) {
        return taskPropertyRepository.save(task);
    }

    public Collection<TaskProperty> findAll() {
        return taskPropertyRepository.findAll();
    }

    public TaskProperty findById(Long id) {
        return taskPropertyRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        taskPropertyRepository.deleteById(id);
    }
}
