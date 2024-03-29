package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDTO {

    private String name;
    private String description;
    private byte[] image;
    private String kindAsString;

    public UserSearchDTO(User user) {
        this.name = user.getFirstName() + " " + user.getLastName();
        this.description = user.getDescription();
        this.image = user.getImage();
        this.kindAsString = "Usuário";
    }

}
