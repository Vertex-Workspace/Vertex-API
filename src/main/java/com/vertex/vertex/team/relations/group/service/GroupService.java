package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.team.relations.group.model.entity.Group;
import com.vertex.vertex.team.relations.group.repository.GroupRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;


    public Group isSubGroup(Long teamId, Long groupId){
        return groupRepository.findByTeam_IdAndId(teamId, groupId);
    }

}
