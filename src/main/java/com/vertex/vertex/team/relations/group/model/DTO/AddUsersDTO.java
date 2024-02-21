package com.vertex.vertex.team.relations.group.model.DTO;

import com.vertex.vertex.user.model.DTO.UserBasicInformationsDTO;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUsersDTO {

    private List<User> users;
}
