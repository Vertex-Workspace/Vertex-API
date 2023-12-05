package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final TeamService teamService;



    public UserTeam findUserTeamByComposeId(Long teamId, Long userId){
        return userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId);
    }

    public List<TeamInfoDTO> findTeamsByUser(Long userID){
        List<TeamInfoDTO> teams = new ArrayList<>();
        for (UserTeam userTeam : userTeamRepository.findAllByUser_Id(userID)) {
            TeamInfoDTO dto = new TeamInfoDTO();
            Team team = userTeam.getTeam();
            BeanUtils.copyProperties(team, dto);
            teamService.addUsers(dto, team);
            teams.add(dto);
        }
        return teams;
    }
}
