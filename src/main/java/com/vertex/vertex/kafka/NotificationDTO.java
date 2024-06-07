package com.vertex.vertex.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotificationDTO {
    private String email;
    private String message;
}
