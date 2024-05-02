package com.vertex.vertex.team.relations.permission.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TypePermissions name;

    @ManyToOne
    @JsonIgnore
    private UserTeam userTeam;

    private boolean enabled;

    private Permission(TypePermissions name, UserTeam userTeam, boolean enabled){
        this.name = name;
        this.userTeam = userTeam;
        this.enabled = enabled;
    }

    public List<Permission> createBasicPermissions(UserTeam userTeam, boolean isCreator){
        return List.of(new Permission(TypePermissions.Criar, userTeam, isCreator),
                new Permission(TypePermissions.Editar, userTeam, isCreator),
                new Permission(TypePermissions.Deletar, userTeam, isCreator));
    }

    public static boolean hasPermission(List<Permission> permissions, TypePermissions type){
        return permissions.stream().map(permission ->
                permission.getName().equals(type) && permission.isEnabled()).findAny().get();
    }
}
