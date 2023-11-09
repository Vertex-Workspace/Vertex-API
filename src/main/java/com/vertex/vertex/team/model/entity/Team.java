package com.vertex.vertex.team.model.entity;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private Date creationDate;

    private Long creatorId;

    @OneToMany(mappedBy = "team")
    private List<Project> projects;

//    @ManyToMany
//    //user -> map
//    private List<User> members;

//    private Image image;

}
