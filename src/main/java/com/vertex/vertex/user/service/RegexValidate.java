package com.vertex.vertex.user.service;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.exception.InvalidPasswordException;
import com.vertex.vertex.user.model.exception.UnsafePasswordException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexValidate {

    public boolean isEmailSecure(String email) {
        return Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                .matcher(email)
                .find();
    }

    public boolean isPasswordSecure(User user, UserDTO userDTO) {
        boolean validPassword = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(user.getPassword())
                .find();

        if (user.getPassword() != null && !validPassword) {
            throw new UnsafePasswordException();
        }

        if (!userDTO.getPassword().equals(userDTO.getPasswordConf())) {
            throw new InvalidPasswordException();
        }
        return validPassword;
    }

}
