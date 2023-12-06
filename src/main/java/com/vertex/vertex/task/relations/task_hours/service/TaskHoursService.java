//package com.vertex.vertex.task.relations.task_hours.service;
//
//import com.vertex.vertex.task.relations.task_hours.repository.TaskHoursRepository;
//import com.vertex.vertex.task.service.TaskService;
//import com.vertex.vertex.task.relations.task_hours.model.DTO.TaskHourEditDTO;
//import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
//import com.vertex.vertex.team.relations.user_team.service.UserTeamService;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@Data
//public class TaskHoursService {
//
//    //Repository
//    private final TaskHoursRepository taskHoursRepository;
//
//    //Useful services
//    private final TaskService taskService;
//    private final UserTeamService userTeamService;
//
//
//    //It must be refactored...
//    public void save(TaskHour taskHour) {
//        //It receives just the initial date, beyond the user and task ID's
////        try {
////            taskHour.setUserTeam(userTeamService.findById(taskHour.getUserTeam().getId()));
////
////            taskHour.setTask(taskService.findById(taskHour.getTask().getId()));
////
////            if(taskHoursRepository.
////                    existsTaskHourByFinalDateNullAndUserTeam_IdAndTask_Id(
////                            taskHour.getUserTeam().getId(), taskHour.getTask().getId())){
////                throw new RuntimeException("O usuário já está trabalhando em cima da tarefa!");
////            }
////
////            //Save or update a row on table task_hours
////            taskHoursRepository.save(taskHour);
////
////            //set the new state of working in this task
////            //The column working_on_task must be referenced when a new row is inserted here
////            taskHour.getUserTeam().setWorkingOnTaskHour(taskHour);
////
////            userTeamService.updateWorkingOnTask(taskHour.getUserTeam());
////        } catch (Exception e){
////            e.printStackTrace();
////            throw e;
////        }
////    }
//    public void save(TaskHourEditDTO taskHourEditDTO){
//        try {
////            userTeamService.findById(taskHourEditDTO.getUserTeam());
////            taskService.findById(taskHourEditDTO.getTask());
////
////            //It understands that I'm editing
////            TaskHour taskHour =
////                    taskHoursRepository.
////                            findByUserTeam_IdAndTask_IdAndFinalDateNull
////                                    (taskHourEditDTO.getUserTeam(), taskHourEditDTO.getTask());
////
////            if(taskHour == null){
////                throw new RuntimeException("A tarefa já foi encerrada");
////            }
////            taskHour.setFinalDate(taskHourEditDTO.getFinalDate());
////
////            //Save or update a row on table task_hours
////            taskHoursRepository.save(taskHour);
////            //set the new state of working in this task
////            //The column working_on_task must be referenced when a new row is inserted here
////            taskHour.getUserTeam().setWorkingOnTaskHour(null);
////            userTeamService.updateWorkingOnTask(taskHour.getUserTeam());
//        } catch (Exception e){
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//
////    public List<TaskHour> findTaskHoursByUserTeamId(Long taskId, Long userId){
//////        return taskHoursRepository.getTaskHoursByTask_IdAndUserTeam_Id(taskId, userId);
////        return null;
////    }
////
////    public List<TaskHour> findAllByTask(Long taskId){
//////        return taskHoursRepository.findAllByTask_Id(taskId);
////        return null;
////    }
//
//}
