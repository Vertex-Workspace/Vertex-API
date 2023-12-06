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

import java.time.LocalDateTime;
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
        System.out.println(LocalDateTime.now());
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
            if(cont == 0){
                taskHoursRepository.save(taskHour);
            }else {
                throw new TaskIsNotFinishedException();
            }
        }
    }
    public void save(TaskHourEditDTO taskHourEditDTO){
        try {
            int cont = 0;
            TaskResponsable taskResponsable = taskResponsablesService.findById(taskHourEditDTO.getTaskResponsable().getId());

            for (int i = 0; i < taskResponsable.getTaskHours().size(); i++) {
                if(taskResponsable.getTaskHours().get(i).getFinalDate() == null) {
                    TaskHour taskHour = taskResponsable.getTaskHours().get(i);
                    BeanUtils.copyProperties(taskHourEditDTO, taskHour);

                    taskResponsable.getTaskHours().get(i).setFinalDate(taskHourEditDTO.getFinalDate());
                    taskHoursRepository.save(taskHour);

                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    public List<TaskHour> findTaskHoursByUserTeamId(Long taskId, Long userId){
//        return taskHoursRepository.getTaskHoursByTask_IdAndUserTeam_Id(taskId, userId);
        return null;
    }

    public List<TaskHour> findAllByTask(Long taskId){
//        return taskHoursRepository.findAllByTask_Id(taskId);
        return null;
    }

}
