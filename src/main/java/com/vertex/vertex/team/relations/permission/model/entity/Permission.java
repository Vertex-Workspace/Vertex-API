package com.vertex.vertex.team.relations.permission.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.team.relations.permission.model.enums.TypePermissions;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
