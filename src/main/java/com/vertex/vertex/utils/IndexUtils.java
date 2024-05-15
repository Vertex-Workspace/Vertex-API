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

    public void updateIndex(Project project, Task task, Long finalIndex){
        List<Task> tasks = sortList(project);

        Task oldIndexTask = tasks.stream().filter(t -> t.getIndexTask().equals(finalIndex)).findAny().get(); //74

        for (Task value : tasks) {
            value.setIndexTask(-1L);
        }

        System.out.println(
                tasks.stream()
                        .map(Task::getId)
                        .toList());
        System.out.println(
                tasks.stream()
                        .map(Task::getIndexTask)
                        .toList());
        System.out.println("Index Final :" + finalIndex); // 2
        System.out.println("Tarefa Mexida: " + task.getId()); // 73




        tasks.get(tasks.indexOf(task)).setIndexTask(finalIndex); // 73 - setIndex = 2

        tasks.get(tasks.indexOf(oldIndexTask)).setIndexTask(finalIndex-1); // 74 - setIndex = 1

        long count = 0;
        for (Task value : tasks) {
            if(value.getIndexTask().equals(-1L)){
                value.setIndexTask(count);
            }
            count++;
        }
        tasks.sort(Comparator.comparingLong(Task::getIndexTask));
        System.out.println(
                tasks.stream()
                        .map(Task::getId)
                        .toList());
        System.out.println(
                tasks.stream()
                .map(Task::getIndexTask)
                .toList());

        taskRepository.saveAll(tasks);
    }

    private List<Task> sortList(Project project){
        List<Task> tasks = new ArrayList<>(project.getTasks());
        tasks.sort(Comparator.comparingLong(Task::getIndexTask));
        return tasks;
    }


}
