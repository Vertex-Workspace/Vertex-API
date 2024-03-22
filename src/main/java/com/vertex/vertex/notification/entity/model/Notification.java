package com.vertex.vertex.notification.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public Notification(String teamName, String projectName, String title, String linkRedirect, User user) {
        this.teamName = teamName;
        this.projectName = projectName;
        this.title = title;
        this.date = LocalDateTime.now();
        this.linkRedirect = linkRedirect;
        this.user = user;
        this.isRead = false;
    }
}
