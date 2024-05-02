package com.vertex.vertex.task.relations.review.service;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.notification.service.NotificationService;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.project.service.ProjectService;
import com.vertex.vertex.security.ValidationUtils;
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
    private final NotificationService notificationService;

    public void finalReview(ReviewCheck reviewCheck) {
        Task task = taskService.findById(reviewCheck.getTaskID());

        Optional<Review> reviewOptional = task.getReviews().stream()
                .filter(review1 -> review1.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)).findFirst();


        if (reviewOptional.isEmpty()) {
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

        if (creator.isEmpty()) {
            throw new RuntimeException("Id do revisador errado!");
        }
        review.setReviewer(creator.get());
        ValidationUtils.validateUserLogged(review.getReviewer().getUserTeam().getUser().getEmail());
        review.setApproveStatus(reviewCheck.getApproveStatus());
        reviewRepository.save(review);

        if (review.getApproveStatus() == ApproveStatus.APPROVED) {
            Task updateTask = review.getTask();
            taskService.setAsDone(updateTask);
        }

        //Notifications
        if (review.getUserThatSentReview().getUserTeam().getUser().getTaskReview()) {
            String status = review.getApproveStatus() == ApproveStatus.APPROVED ? " aprovou " : " reprovou ";

            String title =
                    review.getReviewer().getUserTeam().getUser().getFullName()
                            + " " + status + " " + task.getName() + " com a nota " + review.getGrade() + " (0-5)";

            Notification notification = new Notification(
                    task.getProject(),
                    title,
                    "projeto/" + task.getProject().getId() + "/tarefas?taskID=" + task.getId(),
                    review.getUserThatSentReview().getUserTeam().getUser()
            );
            notificationService.save(notification);
        }
    }

    public void sendToReview(SendToReviewDTO sendToReviewDTO) {
        Task task = taskService.findById(sendToReviewDTO.getTask().getId());

        if (!task.isRevisable()) {
            throw new RuntimeException("A Tarefa não exige entrega!");
        }
        ValidationUtils.loggedUserIsOnTask(task);

        //Validations
        if (task.isNotUnderAnalysis()) {
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


            //Notifications
            if (task.getCreator().getUser().getTaskReview()) {
                String title =
                        review.getUserThatSentReview().getUserTeam().getUser().getFullName()
                                + " entregou " + task.getName();
                Notification notification = new Notification(
                        task.getProject(),
                        title,
                        "projeto/" + task.getProject().getId() + "/tarefas",
                        task.getCreator().getUser()
                );
                notificationService.save(notification);
            }
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

        ValidationUtils.validateUserLogged(loggedUser.getUser().getEmail());

        for (Task task : project.getTasks()) {
            if (task.getCreator().equals(loggedUser) && task.isNotUnderAnalysis()) {
                TaskWaitingToReviewDTO taskWaitingToReviewDTO = new TaskWaitingToReviewDTO(task);

                Optional<Review> currentReview = task.getReviews().stream()
                        .filter(review -> review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS)).findFirst();

                if (currentReview.isPresent()) {
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
        ValidationUtils.loggedUserIsOnTask(task);
        List<ReviewHoursDTO> reviewHoursDTOS = new ArrayList<>();
        for (TaskResponsable taskResponsable : task.getTaskResponsables()) {

            reviewHoursDTOS.add(new ReviewHoursDTO(
                    taskResponsable.getUserTeam().getId(),
                    taskResponsable.getUserTeam().getUser().getFullName(),
                    LocalTime.MIDNIGHT.plus(taskHoursService.calculateTimeOnTask(taskResponsable))));
        }
        return reviewHoursDTOS;
    }


    public void setRevisable(Long taskID, Boolean booleanState) {
        Task task = taskService.findById(taskID);
        ValidationUtils.loggedUserIsOnTaskAndIsCreator(task);
        task.setRevisable(booleanState);
        taskService.save(task);
    }

    public List<Review> getReviewsByProjects(List<Project> projects){
        return projects.stream()
                .flatMap(p -> p.getTasks().stream())
                .flatMap(t -> t.getReviews().stream())
                .toList();
    }
}
