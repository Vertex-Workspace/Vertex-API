package com.vertex.vertex.task.controller;

import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
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
            return new ResponseEntity<>(taskService.save(taskCreateDTO), HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            taskService.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

    @PatchMapping
    public ResponseEntity<?> edit(@RequestBody TaskEditDTO taskEditDTO){
        try{
            return new ResponseEntity<>(taskService.edit(taskEditDTO), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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

    @GetMapping("/info/{taskID}")
    public ResponseEntity<?> getTaskInfos(@PathVariable Long taskID) {
        try
        {
            return new ResponseEntity<>
                    (taskService.getTaskInfos(taskID), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>
                    (e.getMessage(),
                            HttpStatus.NOT_FOUND);
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

    @DeleteMapping("/{taskID}/comment/{commentID}")
    public ResponseEntity<?> deleteComment(@PathVariable Long taskID, @PathVariable Long commentID){
        try{
            return new ResponseEntity<>(taskService.deleteComment(taskID, commentID), HttpStatus.OK);
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

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findAllByUser(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    taskService.getAllByUser(id),
                        HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Usuário não encontrado!",
                        HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<?> findAllByProject(
            @PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    taskService.getAllByProject(id),
                        HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(
                    "Projeto não encontrado!",
                        HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/taskResponsables/{taskId}")
    public ResponseEntity<?> getTaskResponsables(@PathVariable Long taskId){
        try{
            return new ResponseEntity<>(taskService.getTaskResponsables(taskId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/taskResponsables/{taskId}/{teamId}/{userId}")
    public ResponseEntity<?> updateParticipants(@PathVariable Long taskId, @PathVariable Long userId, @PathVariable Long teamId){
        try{
            taskService.editTaskResponsables(taskId, userId, teamId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
