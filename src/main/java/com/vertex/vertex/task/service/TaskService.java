package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.EditValueDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.model.exceptions.NotFoundPropertyInTaskException;
import com.vertex.vertex.task.model.exceptions.NotFoundValueInListException;
import com.vertex.vertex.task.repository.TaskRepository;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
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
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);
        Project project;
        try {
            project = projectService.findById(taskCreateDTO.getProject().getId());
        } catch (Exception e) {
            throw new RuntimeException("There isn't a project with this id is not linked with the current team!");
        }
        //When the task is created, every property is associated with a null value, unless it has a default value
        for (Property property : project.getProperties()) {
            Value currentValue = property.getKind().getValue();
            currentValue.setProperty(property);
            currentValue.setTask(task);
            task.getValues().add(currentValue);
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

    public Task save(EditValueDTO editValueDTO) {
        Task task = null;
        try {
            task = findById(editValueDTO.getId());
        } catch (Exception e) {
            throw new RuntimeException("There isn't a task with this id!");
        }

//        Property property = task.getProject().get
        //pass throughout the list
        //it is a for i and not a for each because if we have a null value, the foreach doesn't identify and don't run
        System.out.println(editValueDTO.getValue().getId());
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
        System.out.println(task);
        return taskRepository.save(task);
    }
}
