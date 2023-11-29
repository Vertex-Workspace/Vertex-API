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
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public User save(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        boolean securePassword = Pattern
                .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(user.getPassword())
                .find();

        if (user.getId() != null && getUserRepository().existsById(user.getId())) {
            throw new RuntimeException("Usuário já existente no banco. Tente novamente");
        } else if (user.getPassword() != null && !securePassword) {
            throw new RuntimeException("Senha Insegura!!! Insira outra senha com base nos nossos requisitos.");
        }
        return userRepository.save(user);
    }

    public User edit(UserEditionDTO userEditionDTO) {
        User user = new User();
        BeanUtils.copyProperties(userEditionDTO, user);
        if (user.getId() != null && !getUserRepository().existsById(user.getId())) {
            throw new RuntimeException("Usuário não existe.");
        }
        User userEmailWithNoEdition = userRepository.findByEmail(user.getEmail());

        if (userEmailWithNoEdition != null && user.getEmail().equals(userEmailWithNoEdition.getEmail())) {
            user.setEmail(userEmailWithNoEdition.getEmail());
            if (userRepository.findByEmail(userEmailWithNoEdition.getEmail()) != null) {
                throw new RuntimeException("Já existe um usuário com esse email no sistema.");
            }
        }

        return userRepository.save(user);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        if (!getUserRepository().existsById(id)) {
            throw new NoSuchElementException("Usuário não existe.");
        }
        return userRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        if (!getUserRepository().existsById(id)) {
            throw new NoSuchElementException("Usuário não existe.");
        } else {
            userRepository.deleteById(id);
        }
    }
}
