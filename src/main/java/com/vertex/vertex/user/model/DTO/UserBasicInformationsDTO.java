package com.vertex.vertex.user.model.DTO;

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
}
