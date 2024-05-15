package com.vertex.vertex.utils;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.DTO.TaskModeViewDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class IndexUtils {
    private final TaskRepository taskRepository;
    public void setIndex(Project project, Task task){
        long index = 0L;
        List<Task> tasks = sortList(project);
        if(!tasks.isEmpty()){
            index = tasks.get(tasks.size()-1).getIndexTask() + 1;
        }
        task.setIndexTask(index);
    }

    public void updateIndex(Project project, List<TaskModeViewDTO> tasks){



        System.out.println(
                tasks.stream()
                        .map(TaskModeViewDTO::getId)
                        .toList());
        System.out.println(
                tasks.stream()
                        .map(TaskModeViewDTO::getIndexTask)
                        .toList());

        long count = 0;
        List<Task> tasksToUpdate = new ArrayList<>();
        for (TaskModeViewDTO value : tasks) {
            for (Task task : project.getTasks()){
                if(value.getId().equals(task.getId())){
                    task.setIndexTask(count);
                    count++;
                    tasksToUpdate.add(task);
                }
            }
        }
        System.out.println(
                project.getTasks().stream()
                        .map(Task::getId)
                        .toList());
        System.out.println(
                project.getTasks().stream()
                        .map(Task::getIndexTask)
                        .toList());

        taskRepository.saveAll(tasksToUpdate);
    }

    private List<Task> sortList(Project project){
        List<Task> tasks = new ArrayList<>(project.getTasks());
        tasks.sort(Comparator.comparingLong(Task::getIndexTask));
        return tasks;
    }


}
