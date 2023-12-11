package com.vertex.vertex.user.model.entity;


import com.vertex.vertex.task.relations.task_hours.model.entity.TaskHour;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Personalization personalization;


}
