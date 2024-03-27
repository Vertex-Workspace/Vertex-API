package com.vertex.vertex.task.controller;

import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.chat.service.ChatService;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.relations.value.model.DTOs.EditValueDTO;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.relations.task_responsables.model.DTOs.TaskResponsablesDTO;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.comment.model.DTO.CommentDTO;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Remove;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{idTask}/chat")
    public Task createChatOfTask(@PathVariable Long idTask){
        Task task = taskService.findById(idTask);
        List<UserTeam> userTeamsList = new ArrayList<>();
        task.getTaskResponsables().forEach(taskResponsable ->{
            userTeamsList.add(taskResponsable.getUserTeam());
        });

        Chat chat = new Chat();
        chat.setName(task.getName());
        chat.setUserTeams(userTeamsList);
        chat.setMessages(new ArrayList<>());
        task.setChatCreated(true);
        task.setChat(chat);
        this.chatService.create(chat);
        return task;
    }
    @PatchMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long id,
            @RequestParam MultipartFile file) {
        try {
            return new ResponseEntity<>
                    (taskService.uploadFile(file, id),
                        HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>
                    (HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{taskId}/remove-file/{fileId}")
    public ResponseEntity<?> uploadFile(
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


}
