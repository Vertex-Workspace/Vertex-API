package com.vertex.vertex.permission.model.entity;


import com.vertex.vertex.permission.model.enums.TypePermissions;
import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TypePermissions name;
    @ManyToOne
    private User user;
//    private Team team;
}
