
package com.vertex.vertex.task.relations.task_responsables.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponsable {

    public TaskResponsable(UserTeam userTeam, Task task) {
        this.userTeam = userTeam;
        this.task = task;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private UserTeam userTeam;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Task task;

    @OneToMany(mappedBy = "taskResponsable", orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TaskHour> taskHours;

}
