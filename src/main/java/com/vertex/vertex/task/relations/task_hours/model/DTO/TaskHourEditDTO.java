package com.vertex.vertex.task.relations.task_hours.model.DTO;


import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class TaskHourEditDTO {

    private Task task;
    private TaskResponsable taskResponsable;

}
