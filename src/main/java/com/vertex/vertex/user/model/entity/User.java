package com.vertex.vertex.user.model.entity;


import com.vertex.vertex.permission.model.entity.PermissionUser;
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
    private String user;
    private String email;
    private String password;
    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;
    @OneToMany(mappedBy = "user")
    private List<PermissionUser> permissionUsers;
}
