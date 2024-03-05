package com.vertex.vertex.user.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.chat.model.Chat;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

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
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, firstName, lastName, email, password, description, location, publicProfile, showCharts);
//    }


}
