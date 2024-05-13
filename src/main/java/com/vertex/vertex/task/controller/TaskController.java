package com.vertex.vertex.task.controller;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.model.DTO.UpdateTaskResponsableDTO;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final ChatService chatService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TaskCreateDTO taskCreateDTO){
        try{
            return new ResponseEntity<>(taskService.save(taskCreateDTO), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            taskService.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/info/{taskID}")
    public ResponseEntity<?> getTaskInfos(@PathVariable Long taskID) {
        try
        {
            return new ResponseEntity<>
                    (taskService.getTaskInfos(taskID), HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
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

    @GetMapping("/{taskID}/task-permission/{userID}")
    public ResponseEntity<?> getTaskPermissions(@PathVariable Long taskID, @PathVariable Long userID){
        try{
            return new ResponseEntity<>(taskService.getTaskPermissions(taskID, userID), HttpStatus.OK);
        } catch(Exception e){
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

    @GetMapping("/user/{userID}")
    public ResponseEntity<?> findAllByUser(
            @PathVariable Long userID) {
        try {
            return new ResponseEntity<>(
                    taskService.getAllByUser(userID),
                        HttpStatus.OK);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    @PatchMapping("/{id}/upload/{userID}")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long id,
            @PathVariable Long userID,
            @RequestParam MultipartFile file) {
        try {
            return new ResponseEntity<>
                    (taskService.uploadFile(file, id, userID),
                        HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{taskId}/remove-file/{fileId}")
    public ResponseEntity<?> deleteFile(
            @PathVariable Long taskId,
            @PathVariable Long fileId) {
        try {
            return new ResponseEntity<>
                    (taskService.removeFile(taskId, fileId),
                            HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/taskResponsables")
    public ResponseEntity<?> updateParticipants(@RequestBody UpdateTaskResponsableDTO updateTaskResponsableDTO){
        try{
            return new ResponseEntity<>(taskService.editTaskResponsables(updateTaskResponsableDTO), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/{idTask}/chat")
    public ResponseEntity<Task> createChatOfTask(@PathVariable Long idTask){
        try{
            return new ResponseEntity<>(chatService.createChatOfTask(idTask),HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{idTask}/chat")
    public Chat getChatOfTask(@PathVariable Long idTask){
        return taskService.findById(idTask).getChat();
    }

    @GetMapping("/query/{query}/{userId}")
    public ResponseEntity<?> findAllByUserAndQuery(
            @PathVariable Long userId, @PathVariable String query) {
        try {
            return new ResponseEntity<>
                    (taskService.findAllByUserAndQuery(userId, query),
                            HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }
    @PatchMapping("/taskDependency/{taskId}/{taskdependencyId}")
    public ResponseEntity<?> setTaskDependency(@PathVariable Long taskId, @PathVariable Long taskdependencyId){
        try{
            return new ResponseEntity<>(taskService.setDependency(taskId, taskdependencyId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/taskDependency/{taskId}")
    public ResponseEntity<?> setTaskDependencyNull(@PathVariable Long taskId){
        try{
            taskService.setTaskDependencyNull(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/doneTask/{projectDependencyId}")
    public ResponseEntity<?> getTasksDone(@PathVariable Long projectDependencyId){
        try {
            return new ResponseEntity<>(taskService.getTasksDone(projectDependencyId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/taskResponsables/{taskId}")
    public ResponseEntity<?> returnResponsables(@PathVariable Long taskId){
        try {
            return new ResponseEntity<>(taskService.returnAllResponsables(taskId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }



}
