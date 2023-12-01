package com.vertex.vertex.task.relations.task_hours.repository;

import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHoursRepository extends JpaRepository<TaskHour, Long> {

//    //Get the hours that the user spent in that task
//    List<TaskHour> getTaskHoursByTask_IdAndUserTeam_Id(Long taskId, Long userTeam);
//
//    List<TaskHour> findAllByTask_Id(Long taskId);
//
//    //Boolean to verify if there is a final date open
//    Boolean existsTaskHourByFinalDateNullAndUserTeam_IdAndTask_Id(Long userTeamId, Long taskId);
//
//    TaskHour findByUserTeam_IdAndTask_IdAndFinalDateNull(Long userTeamId, Long taskId);

}
