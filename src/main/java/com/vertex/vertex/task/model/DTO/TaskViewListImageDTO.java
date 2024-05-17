package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskViewListImageDTO extends TaskModeViewDTO{
    private byte[] image;

    public TaskViewListImageDTO(Task task){
        super(task);
        this.image = task.getCreator().getUser().getImage();
    }
}
