package com.vertex.vertex.task_property.service;


import com.vertex.vertex.property.repository.PropertyRepository;
import com.vertex.vertex.task_property.model.entity.TaskProperty;
import com.vertex.vertex.task_property.repository.TaskPropertyRepository;
import com.vertex.vertex.task_property.value.model.entity.Value;
import com.vertex.vertex.task_property.value.model.entity.ValueText;
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
    private final PropertyRepository propertyRepository;

    public TaskProperty save(TaskProperty task) {
        System.out.println(task);
        task.setProperty(propertyRepository.findById(task.getProperty().getId()).get());
        Value value = task.getProperty().getKind().getValue();
        BeanUtils.copyProperties(task.getValue(), value);
        task.setValue(value);
        if(value instanceof ValueText) {
            ValueText valueText = (ValueText) value;
            System.out.println(valueText);
        }
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
