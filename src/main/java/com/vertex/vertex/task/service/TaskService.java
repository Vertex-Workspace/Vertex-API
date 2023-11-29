package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final PropertyService propertyService;

    public Task save(TaskCreateDTO taskCreateDTO) {
//        System.out.println(task);
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);

        for (TaskProperty taskProperty : task.getTaskProperties()) {
            Property property = propertyService.findById(taskProperty.getProperty().getId());
            Value value = property.getKind().getValue();
            BeanUtils.copyProperties(taskCreateDTO, value);
            taskProperty.setValue(value);
        }
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
