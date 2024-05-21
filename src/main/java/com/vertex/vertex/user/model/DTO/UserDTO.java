package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.model.enums.UserKind;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConf;
    private boolean firstAccess;
    private String image;
    private UserKind userKind;
    private LocalDateTime registerDay;

    private boolean isDefaultUser = false;

    public UserDTO(User user, String imageUrl) {
        System.out.println(user);
        BeanUtils.copyProperties(user, this);
        this.passwordConf = user.getPassword();
        this.firstAccess = true;
        this.image = imageUrl;
        this.userKind = UserKind.GOOGLE;
    }
    public UserDTO(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registerDay = LocalDateTime.now();
        this.firstAccess = false;
        this.isDefaultUser = true;
        this.userKind = UserKind.DEFAULT;
    }


    public UserDTO(
            String firstName, String lastName, String email,
            String password, String passwordConf) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConf = passwordConf;
        this.firstAccess = true;
        this.userKind = UserKind.DEFAULT;
    }
}
