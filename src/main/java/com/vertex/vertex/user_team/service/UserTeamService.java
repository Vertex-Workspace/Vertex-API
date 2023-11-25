package com.vertex.vertex.user_team.service;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserEditionDTO;
import com.vertex.vertex.user_team.model.entity.UserTeam;
import com.vertex.vertex.user_team.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@AllArgsConstructor
@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;

    public UserTeam save(UserDTO userDTO){
        UserTeam user = new UserTeam();
        BeanUtils.copyProperties(userDTO,user);
        return userTeamRepository.save(user);
    }

    public UserTeam save(UserEditionDTO userEditionDTO){
        UserTeam user = new UserTeam();
        BeanUtils.copyProperties(userEditionDTO,user);
        return userTeamRepository.save(user);
    }

    public Collection<UserTeam> findAll(){
        return userTeamRepository.findAll();
    }

    public UserTeam findById(Long id){
        return userTeamRepository.findById(id).get();
    }

    public void deleteById(Long id){
        userTeamRepository.deleteById(id);
    }
}
