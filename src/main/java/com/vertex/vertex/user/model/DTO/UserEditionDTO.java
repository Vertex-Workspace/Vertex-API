package com.vertex.vertex.user.model.DTO;

import com.vertex.vertex.user.model.enums.UserKind;
import com.vertex.vertex.user.relations.personalization.model.entity.Personalization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditionDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConf;
    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;
    private Personalization personalization;
//    @OneToMany(mappedBy = "user")
//    private List<User> permissions;
}
