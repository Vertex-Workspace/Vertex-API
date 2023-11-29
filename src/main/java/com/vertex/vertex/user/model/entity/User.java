package com.vertex.vertex.user.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vertex.vertex.personalization.model.entity.Personalization;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;
    @Column(nullable = false)
    private String password;

    private String description;
    private String location;
    private String image;
    private Boolean publicProfile;
    private Boolean showCharts;
    @ManyToOne(cascade = CascadeType.ALL)
    private Personalization personalization;

}
