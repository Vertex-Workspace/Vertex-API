package com.vertex.vertex.task.relations.task_hours.controller;

import com.vertex.vertex.task.relations.task_hours.model.DTO.TaskHourEditDTO;
import com.vertex.vertex.task.relations.task_hours.model.DTO.TimeInTaskDto;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.task.relations.task_hours.service.TaskHoursService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/task-hours")
public class TaskHoursController {
    private final TaskHoursService taskHoursService;

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskHour>> findTaskHoursByTask(@PathVariable Long taskId){
        try{
            return new ResponseEntity<>(taskHoursService.findTaskHoursByTask(taskId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user-team/{userTeamId}")
    public ResponseEntity<List<TaskHour>> findTaskHoursByUserTeam(@PathVariable Long userTeamId){
        try{
            return new ResponseEntity<>(taskHoursService.findTaskHoursUser(userTeamId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/time-in-task/{idResponsable}")
    public TimeInTaskDto timeInTask(@PathVariable Long idResponsable){
        return taskHoursService.timeInTask(idResponsable);
    }

    @PostMapping("/{idTask}/{idResponsable}")
    public ResponseEntity<?> save(@PathVariable Long idTask, @PathVariable Long idResponsable){
        try{
            taskHoursService.save(idTask,idResponsable);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> update(@RequestBody TaskHourEditDTO taskHourEditDTO){
        try{
            taskHoursService.save(taskHourEditDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
