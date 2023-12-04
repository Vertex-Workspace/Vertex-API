package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.team.model.DTO.TeamHomeDTO;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final TeamService teamService;
    private final UserService userService;



    public UserTeam findUserTeamByComposeId(Long teamId, Long userId){
        return userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId);
    }

    public List<TeamHomeDTO> findTeamsByUser(Long userID){
        List<TeamHomeDTO> teams = new ArrayList<>();
        for (UserTeam userTeam : userTeamRepository.findAllByUser_Id(userID)) {
            TeamHomeDTO teamHomeDTO = new TeamHomeDTO();
            BeanUtils.copyProperties(userTeam.getTeam(), teamHomeDTO);
            teams.add(teamHomeDTO);
        }
        return teams;
    }
}
