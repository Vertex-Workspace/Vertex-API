package com.vertex.vertex.team.relations.user_team.service;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.team.model.DTO.TeamInfoDTO;
import com.vertex.vertex.team.model.DTO.TeamViewListDTO;
import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.relations.user_team.repository.UserTeamRepository;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;

    public void save(UserTeam userTeam){
        userTeamRepository.save(userTeam);
    }

    public UserTeam findUserTeamByComposeId(Long teamId, Long userId){
        return userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId);
    }

    public Boolean findUserInTeam(Team team, Long userId) {
        UserTeam userTeam = userTeamRepository.findByTeam_IdAndUser_Id(team.getId(), userId);
        return userTeam != null;
    }

    public List<TeamViewListDTO> findTeamsByUser(Long userID){
        List<TeamViewListDTO> teams = new ArrayList<>();
        for (UserTeam userTeam : userTeamRepository.findAllByUser_Id(userID)) {
            TeamViewListDTO dto = new TeamViewListDTO();
            Team team = userTeam.getTeam();
            BeanUtils.copyProperties(team, dto);
            teams.add(dto);
        }
        return teams;
    }

    public List<UserTeam> findAllByTeam(Long teamId){
        return userTeamRepository.findAllByTeam_Id(teamId);
    }

    public UserTeam findById(Long userTeamId){
        return userTeamRepository.findById(userTeamId).get();
    }

    public List<UserTeam> findAll(Long id){
        return userTeamRepository.findAllByUser_Id(id);
    }


}
