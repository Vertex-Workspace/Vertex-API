package com.vertex.vertex.task.relations.review.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReviewHoursDTO {
    private Long userTeamID;
    private String username;
    private LocalTime time;
}
