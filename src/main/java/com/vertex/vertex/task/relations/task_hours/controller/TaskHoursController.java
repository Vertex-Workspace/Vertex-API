package com.vertex.vertex.task.relations.task_hours.controller;

import com.vertex.vertex.task.relations.task_hours.model.DTO.TaskHourEditDTO;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/task-hours")
public class TaskHoursController {
    private final TaskHoursService taskHoursService;
    @GetMapping("/{taskId}/user/{userId}")
    public ResponseEntity<List<TaskHour>> findHoursByUserId(@PathVariable Long taskId, @PathVariable Long userId){
        try{
            return new ResponseEntity<>(taskHoursService.findTaskHoursByUserTeamId(taskId, userId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<TaskHour>> findAllByTask(@PathVariable Long taskId){
        try{
            return new ResponseEntity<>(taskHoursService.findAllByTask(taskId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody TaskHour taskHour){
        try{
            taskHoursService.save(taskHour);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    @PatchMapping
    public ResponseEntity<HttpStatus> update(@RequestBody TaskHourEditDTO taskHourEditDTO){
        try{
            taskHoursService.save(taskHourEditDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}