package com.vertex.vertex.task.model.DTO;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnTaskResponsablesDTO {

    private List<User> users;
    private List<Group> groups;
}
