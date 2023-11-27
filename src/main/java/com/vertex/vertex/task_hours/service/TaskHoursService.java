package com.vertex.vertex.task_hours.service;

import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.task_hours.model.entity.TaskHour;
import com.vertex.vertex.task_hours.repository.TaskHoursRepository;
import com.vertex.vertex.user_team.service.UserTeamService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Data
public class TaskHoursService {

    //Repository
    private final TaskHoursRepository taskHoursRepository;

    //Useful services
    private final TaskService taskService;
    private final UserTeamService userTeamService;

    public void save(TaskHour taskHour) {
        //It receives just the initial date, beyond the user and task ID's
        try {
            taskHour.setUserTeam(userTeamService.findById(taskHour.getUserTeam().getId()));

            //To implement after the merge with another branch - Miguel
            taskHour.getUserTeam().setWorkingOnTask(taskHour);
            //The column working_on_task must be referenced when a new row is inserted here

            taskHour.setTask(taskService.findById(taskHour.getTask().getId()));
            taskHoursRepository.save(taskHour);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<TaskHour> findTaskHoursByUserTeamId(Long taskId, Long userId){
        return taskHoursRepository.getTaskHoursByTask_IdAndUserTeam_Id(taskId, userId);
    }

    public List<TaskHour> findAllByTask(Long taskId){
        return taskHoursRepository.findAllByTask_Id(taskId);
    }

}
