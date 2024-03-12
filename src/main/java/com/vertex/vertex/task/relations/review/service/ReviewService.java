package com.vertex.vertex.task.relations.review.service;

import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewCheck;
import com.vertex.vertex.task.relations.review.model.DTO.SendToReviewDTO;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.review.repository.ReviewRepository;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TaskService taskService;

//    public Task saveReview(ReviewCheck reviewCheck) {
//        Task task = findById(reviewCheck.getTask().getId());
//        Review review;
//        TaskResponsable taskResponsable = taskResponsablesRepository.findById(reviewCheck.getReviewer().getId()).get();
//
//        if (reviewCheck.getId() != null) {
//            for (Review reviewFor : task.getReviews()) {
//                if (reviewFor.getId().equals(reviewCheck.getId())) {
//                    review = reviewFor;
//                    task.getReviews().remove(review);
//                    return save(task);
//                }
//            }
//        } else {
//            review = new Review();
//            if (task.getCreator().getId().equals(taskResponsable.getId())) {
//                BeanUtils.copyProperties(reviewCheck, review);
//                task.getReviews().add(review);
//                task.setApproveStatus(reviewCheck.getApproveStatus());
//            } else {
//                throw new RuntimeException("O usuário não é o criador da tarefa");
//            }
//        }
//        return save(task);
//    }

    public Task sendToReview(SendToReviewDTO sendToReviewDTO) {
        Task task = taskService.findById(sendToReviewDTO.getTask().getId());
        //Validations
        if (task.getApproveStatus() == ApproveStatus.DISAPPROVED ||
                task.getApproveStatus() == ApproveStatus.INPROGRESS) {
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
            if(review.getUserThatSentReview() == null){
                throw new RuntimeException("O usuário que enviou a revisão não é um responsável da tarefa");
            }
            //Save the initial Review
            reviewRepository.save(review);

            //Change the state of the task and save
            task.setApproveStatus(ApproveStatus.UNDERANALYSIS);
            return taskService.save(task);
        }
        throw new RuntimeException("A tarefa já foi aprovada. Ela não pode voltar para análise");
    }
}
