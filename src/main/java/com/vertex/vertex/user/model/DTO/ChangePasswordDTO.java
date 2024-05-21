package com.vertex.vertex.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO {

    private Long userId;
    private String newPassword;
    private String oldPassword;

}
