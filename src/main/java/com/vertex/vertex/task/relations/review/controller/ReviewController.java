package com.vertex.vertex.task.relations.review.controller;

import com.vertex.vertex.task.relations.review.model.DTO.ReviewCheck;
import com.vertex.vertex.task.relations.review.model.DTO.SendToReviewDTO;
import com.vertex.vertex.task.relations.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@CrossOrigin
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/review/send")
    public ResponseEntity<?> sendToReview(@RequestBody SendToReviewDTO sendToReviewDTO){
        try{
            reviewService.sendToReview(sendToReviewDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/review/final")
    public ResponseEntity<?> saveReview (@RequestBody ReviewCheck reviewCheck){
        try{
            reviewService.finalReview(reviewCheck);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/review/{userID}/project/{projectID}")
    public ResponseEntity<?> getTasksToReview(@PathVariable Long userID, @PathVariable Long projectID){
        try{
            return new ResponseEntity<>(reviewService.getTasksToReview(userID, projectID), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{taskID}/review/performance")
    public ResponseEntity<?> getPerformanceInTask(@PathVariable Long taskID){
        try{
            return new ResponseEntity<>(reviewService.getPerformanceInTask(taskID), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @PatchMapping("/{taskID}/review/change-state/{booleanState}")
    public ResponseEntity<?> getPerformanceInTask(
            @PathVariable Long taskID,
            @PathVariable Boolean booleanState){
        try{
            reviewService.setRevisable(taskID, booleanState);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
