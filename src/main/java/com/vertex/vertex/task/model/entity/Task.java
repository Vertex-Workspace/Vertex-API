package com.vertex.vertex.task.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.file.model.File;
import com.vertex.vertex.file.model.FileSupporter;
import com.vertex.vertex.notification.entity.model.LogRecord;
import com.vertex.vertex.project.model.ENUM.ProjectReviewENUM;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.DTO.TaskCreateDTO;
import com.vertex.vertex.task.model.DTO.TaskEditDTO;
import com.vertex.vertex.task.model.enums.CreationOrigin;
import com.vertex.vertex.task.relations.comment.model.entity.Comment;
import com.vertex.vertex.task.relations.review.model.ENUM.ApproveStatus;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.task.relations.value.model.entity.Value;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.utils.IndexUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task implements FileSupporter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String googleId;

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

    @ManyToOne(cascade = CascadeType.MERGE)
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Value> values;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<LogRecord> log;

    @ManyToMany
    @JsonIgnore
    private List<Group> groups;

    private Long indexTask;

    @Enumerated(EnumType.STRING)
    private CreationOrigin creationOrigin;

    public String getModifiedAttributeDescription
            (TaskEditDTO dto) {
        if (!Objects.equals(this.name, dto.getName()))
            return "O nome da tarefa foi alterado para " + dto.getName();
        else return "A descrição da tarefa foi alterada";
    }

    public boolean isUnderAnalysis(){
        if(this.getReviews() != null){
            return this.getReviews()
                    .stream()
                    .anyMatch(review -> review.getApproveStatus().equals(ApproveStatus.UNDERANALYSIS));
        }
        return false;
    }

    public Task(TaskCreateDTO dto, Project project, UserTeam creator, IndexUtils indexUtils) {
        BeanUtils.copyProperties(dto, this);
        //Set if the task is revisable or no...
        this.setRevisable(project.getProjectReviewENUM()
                .equals(ProjectReviewENUM.MANDATORY));
        this.creator = creator;
        this.files = new ArrayList<>();
        this.log = (List.of
                (new LogRecord(this,
                        "A tarefa foi criada")));

        indexUtils.setIndex(project, this);
        this.project = project;
        if (Objects.isNull(project.getTasks())) project.setTasks(List.of(this));
        else project.getTasks().add(this);
    }

}