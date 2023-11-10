package com.vertex.vertex.user.model.entity;


import com.vertex.vertex.permission.model.entity.PermissionUser;
import com.vertex.vertex.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;
    private String password;
    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Personalization personalization;
    @OneToMany(mappedBy = "user")
    private List<PermissionUser> permissionUsers;

}
