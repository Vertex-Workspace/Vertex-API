package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.project.model.DTO.ProjectViewListDTO;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewHoursDTO;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoDTO extends TeamViewListDTO{
    private List<User> users;
    private List<ProjectViewListDTO> projects;
    private List<Group> groups;
    private User creator;

    private List<Integer> tasksPerformances = new ArrayList<>();
    private List<ReviewHoursDTO> reviewHoursDTOS = new ArrayList<>();

    private Integer reprovedReviews;
    private Integer approvedReviews;
    private Double averageReviews;
    private Integer percentage;


}
