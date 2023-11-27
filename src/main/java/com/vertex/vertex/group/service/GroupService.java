package com.vertex.vertex.group.service;

import com.vertex.vertex.comment.model.Comment;
import com.vertex.vertex.group.model.dto.GroupDTO;
import com.vertex.vertex.group.model.entity.Group;
import com.vertex.vertex.group.repository.GroupRepository;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user_team.model.dto.UserTeamDTO;
import com.vertex.vertex.user_team.model.entity.UserTeam;
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

    public List<Group> findAll(){
        return groupRepository.findAll();
    }

    public Group findById(Long id){
        return groupRepository.findById(id).get();
    }

    public void deleteById(Long id){
        groupRepository.deleteById(id);
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
