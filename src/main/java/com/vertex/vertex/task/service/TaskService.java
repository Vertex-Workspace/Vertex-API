package com.vertex.vertex.task.service;

import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.property.service.PropertyService;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.TaskPropertyDTO;
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
        for(Value list : task.getValues()){
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

    public TaskPropertyDTO save(TaskPropertyDTO taskPropertyDTO) {
        Task task;
        try {
            task = taskRepository.findById(taskPropertyDTO.getId()).get();
            for (Value list : task.getValues()) {
                if (list.getId() == null) {
                    task.getValues().add(list);
                } else {
                    for (int cont = 0; cont < task.getValues().size(); cont++) {
                        if (task.getValues().get(cont).getId().equals(list.getId())) {
                            task.getValues().set(cont, list);
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
