package com.vertex.vertex.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordForgotDTO implements IPasswordChange {

    private String emailUser;
    private String newPassword;

    @Override
    public String getPassword() {
        return newPassword;
    }
}
