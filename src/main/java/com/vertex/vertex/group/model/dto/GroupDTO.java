package com.vertex.vertex.group.model.dto;

import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.team.model.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private String name;
    private Long groupId;

}
