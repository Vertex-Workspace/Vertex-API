package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public Group edit(Group group){
        return groupRepository.save(group);
    }

    public Group findById(Long groupId){
        return groupRepository.findById(groupId).get();
    }
}
