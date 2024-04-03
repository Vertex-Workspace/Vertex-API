package com.vertex.vertex.log.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exceptionMessage;
    private LocalDateTime timestamp;

    public ExceptionLog(Exception e) {
        this.exceptionMessage = e.getMessage();
        this.timestamp = LocalDateTime.now();
    }

}
