package com.vertex.vertex.task_hours.service;

import com.vertex.vertex.task_hours.model.entity.TaskHour;
import com.vertex.vertex.task_hours.repository.TaskHoursRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
@Data
public class TaskHoursService {

    private final TaskHoursRepository taskHoursRepository;

    public TaskHour save(TaskHour task) {
        return taskHoursRepository.save(task);
    }

    public Collection<TaskHour> findAll() {
        return taskHoursRepository.findAll();
    }

    public TaskHour findById(Long id) {
        return taskHoursRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        taskHoursRepository.deleteById(id);
    }


}
