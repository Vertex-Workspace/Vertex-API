package com.vertex.vertex.notification.entity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private Boolean archive;
    private LocalDateTime date;
    private String linkRedirect;

    public Notification(String title, String message, LocalDateTime date, String linkRedirect) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.linkRedirect = linkRedirect;
    }
}
