package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.DTO.TaskModeViewDTO;
import com.vertex.vertex.task.model.DTO.TaskSearchDTO;
import com.vertex.vertex.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicProfileDTO {
    private Long id;
    private String fullname;
    private String location;
    private String description;
    private String email;
    private byte[] image;
    private LocalTime time;

    private List<Integer> tasksPerformances = new ArrayList<>();
    private List<TaskSearchDTO> tasksInCommon = new ArrayList<>();
}
