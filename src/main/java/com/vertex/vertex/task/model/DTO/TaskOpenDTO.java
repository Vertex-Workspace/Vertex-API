package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.DTO.UserBasicInformationsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

//This DTO is use when the user opens the task
public class TaskOpenDTO {
    private String teamName;
    private String projectName;
    private String creatorFullName;
    private String email;

    private ProjectReviewENUM projectReviewENUM;
}
