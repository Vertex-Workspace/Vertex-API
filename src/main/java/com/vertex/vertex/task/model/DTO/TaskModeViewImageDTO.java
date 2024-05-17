package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
public class TaskModeViewImageDTO extends TaskModeViewDTO{
    private byte[] image;
    public TaskModeViewImageDTO(Task task){
        super(task);
        this.image = task.getCreator().getUser().getImage();
    }

}
