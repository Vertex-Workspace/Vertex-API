package com.vertex.vertex.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO implements IPasswordChange {

    private Long userId;
    private String newPassword;
    private String oldPassword;

    @Override
    public String getPassword() {
        return newPassword;
    }
}
