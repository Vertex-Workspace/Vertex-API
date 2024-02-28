package com.vertex.vertex.user.model.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class UserChatDTO {


    private Long id;
    private String firstName;
    private String lastName;
//  private byte[] photo;


}
