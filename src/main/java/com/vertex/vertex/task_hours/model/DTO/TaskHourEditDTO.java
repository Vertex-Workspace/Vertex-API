package com.vertex.vertex.task_hours.model.DTO;


import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class TaskHourEditDTO {
    private Date finalDate;

    private Long task;

    private Long userTeam;

}
