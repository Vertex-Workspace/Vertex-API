package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskSearchDTO {

    private Long id;
    private String name;
    private String description;
    private String kindAsString;

    public TaskSearchDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.kindAsString = "Task";
    }

}
