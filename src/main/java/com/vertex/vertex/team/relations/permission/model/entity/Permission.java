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
    private UserTeam userTeam;

    private boolean enabled;

    private Permission(TypePermissions name, UserTeam userTeam, boolean enabled){
        this.name = name;
        this.userTeam = userTeam;
        this.enabled = enabled;
    }

    public List<Permission> createBasicPermissions(UserTeam userTeam, boolean isCreator){
        List<Permission> permissions1 = new ArrayList<>();
        Permission permission1 = new Permission(TypePermissions.Criar, userTeam, isCreator);
        Permission permission2 = new Permission(TypePermissions.Editar, userTeam, isCreator);
        Permission permission3 = new Permission(TypePermissions.Deletar, userTeam, isCreator);
        permissions1.add(permission1);
        permissions1.add(permission2);
        permissions1.add(permission3);
        return permissions1;
    }
}
