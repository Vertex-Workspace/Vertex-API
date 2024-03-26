package com.vertex.vertex.notification.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;
    private String projectName;
    private String title;
    private Boolean isRead;
    private LocalDateTime date;
    private String linkRedirect;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private User user;

    public Notification(Project project, String title, String linkRedirect, User user) {
        this.teamName = project.getTeam().getName();
        this.projectName = project.getName();
        this.title = title;
        this.date = LocalDateTime.now();
        this.linkRedirect = linkRedirect;
        this.user = user;
        this.isRead = false;
    }

    public Notification(Team team, String title, String linkRedirect, User user) {
        this.teamName = team.getName();
        this.projectName = "";
        this.title = title;
        this.date = LocalDateTime.now();
        this.linkRedirect = linkRedirect;
        this.user = user;
        this.isRead = false;
    }
}
