package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDTO {

    private String firstName;
    private String lastName;
    private String description;
    private byte[] image;

    public UserSearchDTO(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.description = user.getDescription();
        this.image = user.getImage();
    }

}
