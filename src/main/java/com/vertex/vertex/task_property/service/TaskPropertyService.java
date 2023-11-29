package com.vertex.vertex.task_property.service;


import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.task_property.repository.TaskPropertyRepository;
import com.vertex.vertex.task.value.model.entity.Value;
import com.vertex.vertex.task.value.model.entity.ValueText;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Data
public class TaskPropertyService {

    private final TaskPropertyRepository taskPropertyRepository;
    private final PropertyService propertyService;


    public List<TaskProperty> findAll() {
        return taskPropertyRepository.findAll();
    }

    public TaskProperty findById(Long id) {
        return taskPropertyRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        taskPropertyRepository.deleteById(id);
    }
}
