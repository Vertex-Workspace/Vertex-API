package com.vertex.vertex.user.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditionDTO {
    private Long id;
    private String user;
    private String email;
    private String password;
    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;
//    @OneToMany(mappedBy = "user")
//    private List<User> permissions;
}
