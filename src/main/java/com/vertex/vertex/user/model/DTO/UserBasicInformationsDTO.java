package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.enums.UserKind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInformationsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private UserKind userKind;

}
