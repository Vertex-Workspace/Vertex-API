package com.vertex.vertex.task.relations.review.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSenderDTO {
    private String finalDescription;
    private String username;
    private String email;
    private LocalDateTime date;
}
