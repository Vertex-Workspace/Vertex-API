package com.vertex.vertex.user.service;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserEditionDTO;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.*;
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
        User userEmailWithNoEdition = userRepository.findByEmail(user.getEmail());

        boolean validEmail = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                .matcher(user.getEmail())
                .find();

        if (!validEmail) {
            throw new InvalidEmailException();
        }

        if (userEmailWithNoEdition != null && user.getEmail().equals(userEmailWithNoEdition.getEmail())) {
            user.setEmail(userEmailWithNoEdition.getEmail());

            User userFind = userRepository.findByEmail(userEmailWithNoEdition.getEmail());

            System.out.println(user);
            System.out.println(userFind);

            if (userFind != null && user.getEmail().equals(userFind.getEmail())) {
                throw new EmailAlreadyExistsException();
            }
        }



        boolean securePassword =
                Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                        .matcher(user.getPassword())
                        .find();

        if (user.getPassword() != null && !securePassword) {
            throw new UnsafePasswordException();
        }

        if (!userDTO.getPassword().equals(userDTO.getPasswordConf())) {
            throw new InvalidPasswordException();
        }

        return userRepository.save(user);
    }

    public User edit(UserEditionDTO userEditionDTO) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userEditionDTO, user);
        if (user.getId() != null && !getUserRepository().existsById(user.getId())) {
            throw new RuntimeException("Usuário não existe.");
        }
        User userEmailWithNoEdition = userRepository.findByEmail(user.getEmail());

        if (userEmailWithNoEdition != null && user.getEmail().equals(userEmailWithNoEdition.getEmail())) {
            user.setEmail(userEmailWithNoEdition.getEmail());

            User userFind = userRepository.findByEmail(userEmailWithNoEdition.getEmail());

            System.out.println(user);
            System.out.println(userFind);

            if (userFind != null && user.getEmail().equals(userFind.getEmail())
                    && !user.getId().equals(userFind.getId())) {
                throw new EmailAlreadyExistsException();
            }
        }

        if (!userEditionDTO.getPassword().equals(userEditionDTO.getPasswordConf())) {
            throw new InvalidPasswordException();
        }

        return userRepository.save(user);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        if (!getUserRepository().existsById(id)) {
            throw new UserNotFoundException();
        }
        return userRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        if (!getUserRepository().existsById(id)) {
            throw new UserNotFoundException();
        } else {
            userRepository.deleteById(id);
        }
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User authenticate(UserLoginDTO dto) {
        if (userRepository.existsByEmail
                (dto.getEmail())) {
            User user =
                    userRepository.findByEmail(dto.getEmail());

            if (user.getPassword()
                    .equals(dto.getPassword())) {
                return user;
            }

            throw new IncorrectPasswordException();
        }

        throw new UserNotFoundException();
    }

}
