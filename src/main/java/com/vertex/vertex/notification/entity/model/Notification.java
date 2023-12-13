package com.vertex.vertex.notification.entity.model;

import com.vertex.vertex.notification.entity.Enum.TypeNotification;
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
    @GeneratedValue
    private Long id;
    String message;
    Boolean status;
    LocalDateTime date;


    public Notification(String s, boolean b, LocalDateTime now) {
        this.message = s;
        this.status = b;
        this.date = now;
    }
}
