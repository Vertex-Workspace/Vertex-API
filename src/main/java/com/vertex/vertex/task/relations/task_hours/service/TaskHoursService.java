package com.vertex.vertex.task.relations.task_hours.service;

import com.vertex.vertex.task.relations.task_hours.model.exceptions.TaskIsNotFinishedException;
import com.vertex.vertex.task.relations.task_hours.repository.TaskHoursRepository;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.task_responsables.service.TaskResponsablesService;
import com.vertex.vertex.task.service.TaskService;
import com.vertex.vertex.task.relations.task_hours.model.DTO.TaskHourEditDTO;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final TaskResponsablesService taskResponsablesService;

    public void save(TaskHour taskHour) {
        int cont = 0;
        TaskResponsable taskResponsable = taskResponsablesService.findById(taskHour.getTaskResponsable().getId());
        taskHour.setTaskResponsable(taskResponsable);
        taskResponsable.getTaskHours().add(taskHour);

        for (int i = 0; i < taskResponsable.getTaskHours().size(); i++) {
            if (taskResponsable.getTaskHours().get(i).getFinalDate() == null) {
                cont = cont + 1;
            }
        }
        //we gotta set less one because it is taking the last row with no attributes in database
        cont = cont - 1;
        for (int i = 0; i < taskResponsable.getTaskHours().size(); i++) {
            if (cont == 0) {
                taskHour.setInitialDate(LocalDateTime.now());
                taskHoursRepository.save(taskHour);
            } else {
                throw new TaskIsNotFinishedException();
            }
        }
    }

    public void save(TaskHourEditDTO taskHourEditDTO) {
        int cont = 0;
        TaskResponsable taskResponsable = taskResponsablesService.findById(taskHourEditDTO.getTaskResponsable().getId());

        for (int i = 0; i < taskResponsable.getTaskHours().size(); i++) {
            if (taskResponsable.getTaskHours().get(i).getFinalDate() == null) {
                TaskHour taskHour = taskResponsable.getTaskHours().get(i);
                BeanUtils.copyProperties(taskHourEditDTO, taskHour);

                taskResponsable.getTaskHours().get(i).setFinalDate(LocalDateTime.now());
                BeanUtils.copyProperties(taskHourEditDTO, taskHour);
                //calculate the difference between the initial and final date and set as timeSpent;
                Duration timeSpentInTask = Duration.between(taskResponsable.getTaskHours().get(i).getInitialDate(),
                        taskResponsable.getTaskHours().get(i).getFinalDate());
                taskResponsable.getTaskHours().get(i).setTimeSpent(LocalTime.ofNanoOfDay(timeSpentInTask.toNanos()));

                taskHoursRepository.save(taskHour);

            }
        }
    }

    public LocalTime timeInTask(TaskHourEditDTO taskHourEditDTO) {
        TaskResponsable taskResponsable = taskResponsablesService.findById(taskHourEditDTO.getTaskResponsable().getId());

        Duration duracaoTotal = Duration.ZERO;
        //sum all the time spent of one taskResponsable
        for (int i = 0; i < taskResponsable.getTaskHours().size(); i++) {
            duracaoTotal = duracaoTotal.plusHours(taskResponsable.getTaskHours().get(i).getTimeSpent().getHour())
                    .plusMinutes(taskResponsable.getTaskHours().get(i).getTimeSpent().getMinute())
                    .plusSeconds(taskResponsable.getTaskHours().get(i).getTimeSpent().getSecond());
        }
        return LocalTime.MIDNIGHT.plus(duracaoTotal);
    }


    public List<TaskHour> findTaskHoursByUserTeamId(Long taskId, Long userId) {
//        return taskHoursRepository.getTaskHoursByTask_IdAndUserTeam_Id(taskId, userId);
        return null;
    }

    public List<TaskHour> findAllByTask(Long taskId) {
//        return taskHoursRepository.findAllByTask_Id(taskId);
        return null;
    }

}
