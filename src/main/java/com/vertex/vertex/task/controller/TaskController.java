package com.vertex.vertex.task.controller;

import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewCheck;
import com.vertex.vertex.task.relations.review.model.DTO.SetFinishedTask;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    @GetMapping
    public ResponseEntity<List<Task>> findAllByProject(){
        try{
            return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TaskCreateDTO taskCreateDTO){
        try{
            taskService.save(taskCreateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Property> delete(@PathVariable Long id){
        try{
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/value")
    public ResponseEntity<?> save(@RequestBody EditValueDTO editValueDTO){
        try{
            return new ResponseEntity<>(taskService.save(editValueDTO), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> saveComment (@RequestBody CommentDTO commentDTO){
        try{
            return new ResponseEntity<>(taskService.saveComment(commentDTO), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/review")
    public ResponseEntity<?> saveReview (@RequestBody ReviewCheck reviewCheck){
        try{
            return new ResponseEntity<>(taskService.saveReview(reviewCheck), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/send-to-review")
    public ResponseEntity<?> saveToReview (@RequestBody SetFinishedTask setFinishedTask){
        try{
            return new ResponseEntity<>(taskService.taskUnderAnalysis(setFinishedTask), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/responsables")
    public ResponseEntity<?> saveResponsables (@RequestBody TaskResponsablesDTO taskResponsable){
        try{
            return new ResponseEntity<>(taskService.saveResponsables(taskResponsable), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
