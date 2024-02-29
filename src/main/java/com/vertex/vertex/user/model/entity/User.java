package com.vertex.vertex.user.model.entity;


import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @Lob
    @Column(name = "image",
            columnDefinition = "BLOB")
    @ToString.Exclude
    private byte[] image;

    private Boolean publicProfile;

    private Boolean showCharts;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Notification> notificationList;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Personalization personalization;

}
