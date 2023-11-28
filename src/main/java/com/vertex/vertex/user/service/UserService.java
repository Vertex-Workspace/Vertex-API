package com.vertex.vertex.user.service;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserEditionDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public User save(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return userRepository.save(user);
    }

    public User save(UserEditionDTO userEditionDTO){
        User user = new User();
        BeanUtils.copyProperties(userEditionDTO,user);
        return userRepository.save(user);
    }

    public Collection<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id).get();
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}
