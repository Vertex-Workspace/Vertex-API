package com.vertex.vertex.chat.relations.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketMessage;

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
    @ToString.Exclude
    private Chat chat;

    private String user;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageUser;

    private String contentMessage;
    private LocalDateTime time;
    private boolean visualized;

//  private MultipartFile multipartFile;

}
