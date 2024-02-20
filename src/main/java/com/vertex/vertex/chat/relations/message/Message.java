package com.vertex.vertex.chat.relations.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Chat chat;

    private String contentMessage;
    private LocalDateTime time;
    private boolean visualized;

//  private MultipartFile multipartFile;

}
