package com.eshop.UserService.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eshop_users")
@Data
@NoArgsConstructor
public class UserProfile {

    @Id
    @Column(name = "buyerId")
    private String buyerId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "keycloak_id", nullable = false)
    private String keycloakId;
}