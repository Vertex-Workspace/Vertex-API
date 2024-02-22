package com.vertex.vertex.task.relations.task_hours.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeInTaskDto {

    private LocalTime timeInTask;
    private boolean working;

}
