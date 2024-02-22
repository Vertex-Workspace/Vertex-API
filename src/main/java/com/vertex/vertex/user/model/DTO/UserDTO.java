package com.vertex.vertex.user.model.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String image;

}
