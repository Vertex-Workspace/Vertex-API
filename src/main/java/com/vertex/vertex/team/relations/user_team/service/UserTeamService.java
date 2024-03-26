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


import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;

    public void save(UserTeam userTeam){
        userTeamRepository.save(userTeam);
    }

    public UserTeam findUserTeamByComposeId(Long teamId, Long userId){
        Optional<UserTeam> userTeam = userTeamRepository.findByTeam_IdAndUser_Id(teamId, userId);
        if(userTeam.isPresent()){
            return userTeam.get();
        }
        throw new RuntimeException("User Team Not Found!");
    }

    public Boolean findUserInTeam(Team team, Long userId) {
        UserTeam userTeam = null;
        try {
            findUserTeamByComposeId(team.getId(), userId);
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public List<UserTeam> findAllByUserAndQuery(Long userId, String query) {
        return userTeamRepository
                .findAllByUser_IdAndTeam_Name(userId, query);
    }

    public List<UserTeam> findAllByUser(Long userId) {
        return userTeamRepository
                .findAllByUser_Id(userId);
    }


}
