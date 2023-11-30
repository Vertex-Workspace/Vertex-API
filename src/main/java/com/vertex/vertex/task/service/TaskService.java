package com.vertex.vertex.task.service;

import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.EditValueDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Data
@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final PropertyService propertyService;

    public Task save(TaskCreateDTO taskCreateDTO) {
        System.out.println("entrou");
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);
        for (Value list : task.getValues()) {
            list.setTask(task);
        }
        System.out.println(task.getValues());
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

    public Task save(EditValueDTO editValueDTO) {
        Task task = taskRepository.findById(editValueDTO.getId()).get();
        //pass throughout the list
        //it is a for i and not a for each because if we have a null value, the foreach doesn't identify and don't run
        for (int i = 0; i < task.getValues().size(); i++) {
            if (task.getValues().get(i).getId().equals(editValueDTO.getId())) {
                Property property = propertyService.findById(editValueDTO.getValue().getProperty().getId());
                Value currentValue = property.getKind().getValue();
                currentValue.setId(editValueDTO.getId());
                currentValue.setTask(task);
                currentValue.setProperty(property);
                currentValue.setValue(editValueDTO.getValue().getValue());
                task.getValues().set(i, currentValue);
            }
        }
        return taskRepository.save(task);
    }
}
