package com.vertex.vertex.team.model.DTO;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.group.model.entity.Group;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO extends TeamPatchDefaultDTO {
    private String name;

    private Long teamId;

    private Long fatherGroupId;
}
