package com.vertex.vertex.task.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.TaskPropertyDTO;
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
import java.util.NoSuchElementException;

import static com.vertex.vertex.property.model.ENUM.PropertyKind.LIST;
import static com.vertex.vertex.property.model.ENUM.PropertyKind.STATUS;

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
        for(TaskProperty list : task.getTaskProperties()){
            list.setTask(task);
        }
        System.out.println(task.getTaskProperties());
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

    public TaskPropertyDTO save(TaskPropertyDTO taskPropertyDTO) {
        Task task;
        try {
            task = taskRepository.findById(taskPropertyDTO.getId()).get();
            for (TaskProperty list : task.getTaskProperties()) {
                if (list.getId() == null) {
                    task.getTaskProperties().add(list);
                } else {
                    for (int cont = 0; cont < task.getTaskProperties().size(); cont++) {
                        if (task.getTaskProperties().get(cont).getId().equals(list.getId())) {
                            task.getTaskProperties().set(cont, list);
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
        return taskPropertyDTO;

    }


}
