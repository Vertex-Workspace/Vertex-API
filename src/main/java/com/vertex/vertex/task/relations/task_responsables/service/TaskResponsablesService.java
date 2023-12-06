package com.vertex.vertex.task.relations.task_responsables.service;

import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.repository.TaskResponsablesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskResponsablesService {

    private final TaskResponsablesRepository taskResponsablesRepository;

    public TaskResponsable findById(Long id){
        return taskResponsablesRepository.findById(id).get();
    }

    public List<TaskResponsable> findAll(){
        return taskResponsablesRepository.findAll();
    }
}
