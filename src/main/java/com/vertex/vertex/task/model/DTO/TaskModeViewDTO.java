package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModeViewDTO {

    private Long id;
    private String name;
    @ToString.Exclude
    private List<Value> values;
    private Long indexTask;
    private String googleId;

    public TaskModeViewDTO(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.values = task.getValues();
        this.indexTask = task.getIndexTask();
        this.googleId = task.getGoogleId();
    }
}
