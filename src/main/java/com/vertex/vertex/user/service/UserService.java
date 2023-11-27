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
import java.util.NoSuchElementException;

@Data
@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public User save(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        if (user.getId()!= null && getUserRepository().existsById(user.getId())){
            throw new RuntimeException("Usuário já existente no banco. Tente novamente");
        }
        return userRepository.save(user);
    }

    public User save(UserEditionDTO userEditionDTO){
        User user = new User();
        BeanUtils.copyProperties(userEditionDTO,user);
        if (user.getId()!= null && !getUserRepository().existsById(user.getId())){
            throw new RuntimeException("Usuário não existe.");
        }
        return userRepository.save(user);
    }

    public Collection<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        if (!getUserRepository().existsById(id)){
            throw new NoSuchElementException("Usuário não existe.");
        }
        return userRepository.findById(id).get();
    }

    public void deleteById(Long id){
        if (!getUserRepository().existsById(id)){
            throw new NoSuchElementException("Usuário não existe.");
        }else {
            userRepository.deleteById(id);
        }
    }
}
