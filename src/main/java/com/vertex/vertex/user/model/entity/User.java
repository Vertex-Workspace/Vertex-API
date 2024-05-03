package com.vertex.vertex.user.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.notification.entity.model.Notification;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Provider;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
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
    @JsonIgnore
    private String password;

    private String description;

    private String location;

    private boolean firstAccess;

    private boolean defaultSettings;

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

    public User(OAuth2User user, String email) {
        String lastName = user.getAttribute("family_name");
        String firstName = user.getAttribute("name");
        firstName = firstName.substring(0, firstName.indexOf(" "));
        System.out.println(user.getClass());
        this.email = email;
        setPassword(email);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
