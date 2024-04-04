package com.vertex.vertex.user.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User{
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

    private boolean firstAccess;

    @Lob
    @Column(name = "image",
            columnDefinition = "LONGBLOB")
    @ToString.Exclude
    private byte[] image;

    private Boolean publicProfile;

    private Boolean showCharts;

    @OneToMany(orphanRemoval = true, mappedBy ="user")
    @JsonIgnore
    @ToString.Exclude
    private List<Notification> notifications;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Personalization personalization;

    //Notifications
    private Boolean taskReview;
    private Boolean newMembersAndGroups;
    private Boolean permissionsChanged;
    private Boolean responsibleInProjectOrTask;
    private Boolean anyUpdateOnTask;
    private Boolean sendToEmail;


    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

}
