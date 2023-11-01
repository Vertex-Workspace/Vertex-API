package com.vertex.vertex.comment.model;

import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.xml.crypto.Data;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Data date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Task task;
}
