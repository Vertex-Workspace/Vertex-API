package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.file.model.File;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class TaskModeViewDTO {

    private Long id;
    private String name;
    @ToString.Exclude
    private List<Value> values;
    private Long indexTask;
    @ToString.Exclude
    private byte[] image;

    public TaskModeViewDTO(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.values = task.getValues();
        this.image = task.getCreator().getUser().getImage();
        this.indexTask = task.getIndexTask();
    }
}
