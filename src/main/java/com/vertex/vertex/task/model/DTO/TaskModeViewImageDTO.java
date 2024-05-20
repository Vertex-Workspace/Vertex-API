package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
public class TaskModeViewImageDTO extends TaskModeViewDTO{
    private byte[] image;
    private String imgUrl;
    public TaskModeViewImageDTO(Task task){
        super(task);
        User creator = task.getCreator().getUser();
        if(creator.getImage() != null){
            this.image = creator.getImage();
        } else {
            this.imgUrl = creator.getImgUrl();
        }
    }

}
