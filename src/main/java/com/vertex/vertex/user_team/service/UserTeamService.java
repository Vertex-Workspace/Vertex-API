package com.vertex.vertex.user_team.service;

import com.vertex.vertex.team.model.entity.Team;
import com.vertex.vertex.team.service.TeamService;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import com.vertex.vertex.user_team.model.dto.UserTeamDTO;
import com.vertex.vertex.user_team.model.dto.UserTeamEditionDTO;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import com.vertex.vertex.user_team.repository.UserTeamRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final TeamService teamService;
    private final UserService userService;

    public UserTeam save(UserTeamDTO utdto) {
        if (teamService.existsById(utdto.getTeamId())
                && userService.existsById(utdto.getUserId())) {
            UserTeam ut = copyProps(utdto);

            return userTeamRepository.save(ut);
        }

        throw new EntityNotFoundException();
    }

    public UserTeam save(UserTeamEditionDTO utdto) {

        if (teamService.existsById(utdto.getTeamId())
                && userService.existsById(utdto.getUserId())) {

            UserTeam ut = copyProps(utdto);
            return userTeamRepository.save(ut);
        }
        throw new EntityNotFoundException();
    }

    public List<UserTeam> findAll(){
        return userTeamRepository.findAll();
    }

    public UserTeam findById(Long id){
        if (userTeamRepository.existsById(id)) {
            return userTeamRepository.findById(id).get();
        }
        throw new EntityNotFoundException();
    }

    public void deleteById(Long id){
        if (userTeamRepository.existsById(id)) {
            userTeamRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public UserTeam copyProps(
            UserTeamDTO utdto) {
        UserTeam ut = new UserTeam();
        Team team = teamService.findById(utdto.getTeamId());
        User user = userService.findById(utdto.getUserId());
        BeanUtils.copyProperties(utdto, ut);
        ut.setTeam(team);
        ut.setUser(user);
        return ut;
    }
}
