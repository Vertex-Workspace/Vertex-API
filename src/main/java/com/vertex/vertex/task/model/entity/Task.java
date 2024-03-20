package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class  Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    private String name;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskResponsable> taskResponsables;

    @Column(length = 1000)
    private String description;

    private boolean isRevisable;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private UserTeam creator;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Project project;

    @OneToOne
    private Task taskDependency;

    @OneToMany
    private List<Task> subTasks;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private List<Value> values;


    public boolean isUnderAnalysis(){
        if(this.getReviews() != null){
            return this.getReviews()
                    .stream()
                    .map(review -> review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS))
                    .isParallel();
        }
        return false;
    }
}
