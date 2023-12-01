package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.task.relations.comment.model.Comment;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.Value;
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    private String name;

    @OneToMany
    private List<UserTeam> responsables;

    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Project project;

    @OneToOne
    private Task taskDependency;

    @OneToMany
    private List<Task> subTasks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Value> values;

}
