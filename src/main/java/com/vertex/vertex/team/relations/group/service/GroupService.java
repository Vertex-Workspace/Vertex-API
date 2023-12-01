package com.vertex.vertex.team.relations.group.service;

import com.vertex.vertex.team.relations.group.model.dto.GroupDTO;
import com.vertex.vertex.team.relations.group.model.dto.GroupEditionDTO;
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
    private final TeamService teamService;

    public Group save(Long teamId, GroupDTO dto) {
        if (teamService.existsById(teamId)) {
            Group gr = copyProps(dto, teamId);
            return groupRepository.save(gr);
        }
        throw new EntityNotFoundException();
    }

    public Group save(Long teamId, GroupEditionDTO dto) {
        if (teamService.existsById(teamId)
                && groupRepository.existsById(dto.getId())) {
            Group gr = copyProps(dto, teamId);
            return groupRepository.save(gr);
        }
        throw new EntityNotFoundException();
    }

    public List<Group> findAll(){
        return groupRepository.findAll();
    }

    public Group findById(Long id){
        if (groupRepository.existsById(id)) {
            return groupRepository.findById(id).get();
        }
        throw new EntityNotFoundException();
    }

    public void deleteById(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);

        } else {
            throw new EntityNotFoundException();
        }
    }

    public Group copyProps(
            GroupDTO dto, Long teamId) {
        Group gr = new Group();
        Team team = teamService.findById(teamId);

        try {
            Group grSuper = groupRepository.findById
                    (dto.getGroupId()).get();
            gr.setGroup(grSuper);
        } catch (Exception ignored) {}

        BeanUtils.copyProperties(dto, gr);
        gr.setTeam(team);

        return gr;
    }

}
