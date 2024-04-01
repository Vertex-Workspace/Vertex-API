package com.vertex.vertex.notification.entity;

import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class NotificationWebSocketDTO {
    private Long id;
    private String teamName;
    private String projectName;
    private String title;
    private Boolean isRead;
    private LocalDateTime date;
    private String linkRedirect;
    private User user;
    private String sessionWebSocketID;

}
