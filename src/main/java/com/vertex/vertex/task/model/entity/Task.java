package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.model.FileSupporter;
import com.vertex.vertex.notification.entity.model.LogRecord;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
public class Task implements FileSupporter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 55)
    private String name;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskResponsable> taskResponsables;

    @Column(length = 1000)
    private String description;

    private boolean isRevisable;

    @ManyToOne
    @ToString.Exclude
    private UserTeam creator;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Project project;

    @ManyToOne
    @ToString.Exclude
    private Task taskDependency;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;

    boolean chatCreated;

    @OneToMany
    private List<Task> subTasks;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private List<Value> values;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<LogRecord> log = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    List<Group> groups;

    public String getModifiedAttributeDescription
            (TaskEditDTO dto) {
        if (!Objects.equals(this.name, dto.getName()))
            return "O nome da tarefa foi alterado para " + dto.getName();
        else return "A descrição da tarefa foi alterada";
    }

    public boolean isNotUnderAnalysis(){
        if(this.getReviews() != null){
            return !this.getReviews()
                    .stream()
                    .map(review -> review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS))
                    .isParallel();
        }
        return true;
    }

    public Task() {
        setLog(List.of
                (new LogRecord(this,
                        "A tarefa foi criada")));

    }
}
