package com.vertex.vertex.comment.model;

import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Date date;
    @ManyToOne
    private User user;
    @ManyToOne
    private TaskProperty task;
}
