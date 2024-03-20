package com.vertex.vertex.task.relations.review.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.task.model.DTO.TaskWaitingToReviewDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewCheck;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewHoursDTO;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewSenderDTO;
import com.vertex.vertex.task.relations.review.model.DTO.SendToReviewDTO;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.review.repository.ReviewRepository;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final TaskHoursService taskHoursService;
    public void finalReview(ReviewCheck reviewCheck) {
        Task task = taskService.findById(reviewCheck.getTaskID());

        Optional<Review> reviewOptional = task.getReviews().stream()
                .filter(review1 -> review1.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)).findFirst();


        if(reviewOptional.isEmpty()){
            throw new RuntimeException("Nenhuma revisão em aberto!");
        }

        Review review = reviewOptional.get();


        review.setReviewDate(LocalDateTime.now());
        review.setGrade(reviewCheck.getGrade());
        review.setFinalDescription(reviewCheck.getFinalDescription());

        Optional<TaskResponsable> creator = review.getTask().getTaskResponsables()
                .stream().filter(
                        taskResponsable -> taskResponsable.getUserTeam().getUser().getId().equals(reviewCheck.getReviewerID()))
                .findFirst();
        if(creator.isEmpty()){
            throw new RuntimeException("Id do revisador errado!");
        }
        review.setCreatorReviewer(creator.get());
        review.setApproveStatus(reviewCheck.getApproveStatus());
        reviewRepository.save(review);
    }

    public void sendToReview(SendToReviewDTO sendToReviewDTO) {
        Task task = taskService.findById(sendToReviewDTO.getTask().getId());
        //Validations
        if (!task.isUnderAnalysis()) {
            Review review = new Review();
            //Description
            review.setDescription(sendToReviewDTO.getDescription());
            //Task
            review.setTask(task);
            //UserThatSentReview
            task.getTaskResponsables().forEach(taskResponsable -> {
                if (taskResponsable.getId().equals(sendToReviewDTO.getUserThatSentReview().getId())) {
                    review.setUserThatSentReview(taskResponsable);
                }
            });
            if (review.getUserThatSentReview() == null) {
                throw new RuntimeException("O usuário que enviou a revisão não é um responsável da tarefa");
            }
            review.setApproveStatus(ApproveStatus.UNDERANALYSIS);
            review.setSentDate(LocalDateTime.now());
            //Save the initial Review
            reviewRepository.save(review);
        } else {
            throw new RuntimeException("A tarefa já foi aprovada. Ela não pode voltar para análise");
        }
    }

    public List<TaskWaitingToReviewDTO> getTasksToReview(Long userID, Long projectID) {
        List<TaskWaitingToReviewDTO> tasks = new ArrayList<>();

        Project project = projectService.findById(projectID);

        UserTeam loggedUser = project.getTeam().getUserTeams()
                .stream()
                .filter(userTeam -> userTeam.getUser().getId().equals(userID))
                .findFirst()
                .get();

        for (Task task : project.getTasks()){
            if(task.getCreator().equals(loggedUser) && !task.isUnderAnalysis()){
                TaskWaitingToReviewDTO taskWaitingToReviewDTO = new TaskWaitingToReviewDTO(task);

                Optional<Review> currentReview = task.getReviews().stream()
                        .filter(review -> review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)).findFirst();

                if(currentReview.isPresent()){
                    Review review = currentReview.get();

                    taskWaitingToReviewDTO.setSender(
                            new ReviewSenderDTO(
                                    review.getDescription(),
                                    review.getUserThatSentReview().getUserTeam().getUser().getFullName(),
                                    review.getUserThatSentReview().getUserTeam().getUser().getEmail(),
                                    review.getReviewDate()
                            )

                    );
                    taskWaitingToReviewDTO.setReviewHours(getPerformanceInTask(task.getId()));

                    tasks.add(taskWaitingToReviewDTO);
                }
            }
        }
        return tasks;

    }

    public List<ReviewHoursDTO> getPerformanceInTask(Long taskID) {
        Task task = taskService.findById(taskID);
        List<ReviewHoursDTO> reviewHoursDTOS = new ArrayList<>();
        for (TaskResponsable taskResponsable : task.getTaskResponsables()) {

            reviewHoursDTOS.add(new ReviewHoursDTO(
                    taskResponsable.getUserTeam().getId(),
                    taskResponsable.getUserTeam().getUser().getFullName(),
                    LocalTime.MIDNIGHT.plus(taskHoursService.calculateTimeOnTask(taskResponsable))));
        }
        return reviewHoursDTOS;
    }

    public Review findById(Long reviewID){
        Optional<Review> review = reviewRepository.findById(reviewID);
        if(review.isPresent()){
            return review.get();
        }
        throw new RuntimeException("Review não existe!");

    }

    public void setRevisable(Long taskID, Boolean booleanState){
        Task task = taskService.findById(taskID);
        task.setRevisable(booleanState);
        taskService.save(task);
    }
}
