package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

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

    public UserDTO(User user, String imageUrl) {
        BeanUtils.copyProperties(user, this);
        this.passwordConf = user.getPassword();
        this.firstAccess = true;
        this.image = imageUrl;
    }

}
