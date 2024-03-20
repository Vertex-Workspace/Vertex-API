package com.vertex.vertex.task.model.DTO;


import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEditDTO {
    private Long id;
    private String name;
    private String description;
}
