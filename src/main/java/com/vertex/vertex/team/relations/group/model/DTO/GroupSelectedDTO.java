package com.vertex.vertex.team.relations.group.model.DTO;

import com.vertex.vertex.user.model.DTO.UserBasicInformationsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSelectedDTO extends GroupRegisterDTO{

    private List<UserBasicInformationsDTO> userInfos;
}
