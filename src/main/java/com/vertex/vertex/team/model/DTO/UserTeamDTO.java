package com.vertex.vertex.team.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamDTO extends TeamPatchDefaultDTO {
    private Long teamId;
    private Long userId;
}
