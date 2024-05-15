package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskIndexDTO {
    private Long taskID;
    private Long finalIndex;
}
