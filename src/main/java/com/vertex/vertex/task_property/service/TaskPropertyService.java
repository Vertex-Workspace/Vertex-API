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

    public TaskProperty save(TaskProperty task) {
        System.out.println(task);
        task.setProperty(propertyService.findById(task.getProperty().getId()));
        System.out.println(task.getValue());

        Value value = task.getProperty().getKind().getValue();
        if (value instanceof ValueText) {
            ValueText valueText = (ValueText) value;
            System.out.println(valueText.getText());
            ((ValueText) value).setText(valueText.getText());
        }

        System.out.println(value);
        BeanUtils.copyProperties(task.getValue(), value);
        task.setValue(value);
        return taskPropertyRepository.save(task);
    }

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
