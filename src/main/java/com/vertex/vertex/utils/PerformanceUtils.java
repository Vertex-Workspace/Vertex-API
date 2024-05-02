package com.vertex.vertex.utils;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.property.model.ENUM.PropertyKind;
import com.vertex.vertex.property.model.ENUM.PropertyListKind;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewHoursDTO;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PerformanceUtils {
    private final TaskHoursService taskHoursService;

    //Return the amount of task Todo, Doing and Done
    public List<Integer> tasksSplitByCategory(List<Project> projects){
        //[0] - TODO
        //[1] - DOING
        //[2] - DONE
        List<Integer> tasksCategory = new ArrayList<>();
        List<PropertyListKind> listKinds = List.of(PropertyListKind.TODO, PropertyListKind.DOING, PropertyListKind.DONE);

        for (PropertyListKind propertyListKind : listKinds) {
            tasksCategory.add(projects.stream()
                            .flatMap(p -> p.getTasks().stream())
                            .flatMap(t -> t.getValues().stream())
                            .filter(value -> value.getProperty().getKind().equals(PropertyKind.STATUS)
                                    && ((PropertyList) value.getValue()).getPropertyListKind().equals(propertyListKind))
                            .toList().size());
        }
        return tasksCategory;
    }


    //[0] - TODO
    //[1] - DOING
    //[2] - DONE
    public int calculatePercentage(List<Integer> tasks){
        int todoTasks = tasks.get(0);
        int doingTasks = tasks.get(1);
        int doneTasks = tasks.get(2);

        int percentage = 0;
        if(doneTasks > 0){
            if(todoTasks > 0 || doingTasks > 0){
                percentage = (doneTasks * 100) / (doneTasks + todoTasks + doingTasks);
            } else {
                percentage = 100;
            }
        }
        return percentage;

    }

    public List<ReviewHoursDTO> getTimeOnTasks(Team team){
        List<ReviewHoursDTO> reviews = new ArrayList<>();
        for (UserTeam userTeam : team.getUserTeams()) {
            List<TaskResponsable> tasksResponsible = team.getProjects().stream()
                    .flatMap(p -> p.getTasks().stream())
                    .flatMap(t -> t.getTaskResponsables().stream())
                    .filter(taskResponsable -> taskResponsable.getUserTeam().equals(userTeam))
                    .toList();

            Duration duration = Duration.ZERO;
            for (TaskResponsable taskResponsable : tasksResponsible) {
                duration = duration.plus(taskHoursService.calculateTimeOnTask(taskResponsable));
            }

            reviews.add(new ReviewHoursDTO(
                    userTeam.getId(),
                    userTeam.getUser().getFullName(),
                    LocalTime.MIDNIGHT.plus(duration)
            ));
        }
        return reviews;
    }

    public Integer getReviewsByStatus(List<Review> reviews, ApproveStatus approveStatus){
        return reviews.stream()
                .filter(r -> r.getApproveStatus().equals(approveStatus))
                .toList()
                .size();
    }

    public double calculateAverage(List<Review> reviews){
        List<Review> reviewsWithGrade = reviews.stream()
                .filter(r -> r.getGrade() != null)
                .toList();

        double finalSumGrade =
                reviewsWithGrade.stream()
                .mapToDouble(Review::getGrade)
                .sum();

        int amountOfGradeReview = reviewsWithGrade.size();
        if(finalSumGrade > 0){
            return finalSumGrade / amountOfGradeReview;
        }
        return 0.0;
    }
}
