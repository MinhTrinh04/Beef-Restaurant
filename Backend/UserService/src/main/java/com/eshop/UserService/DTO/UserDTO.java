package com.eshop.UserService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String buyerId;
    private String email;
    private String name;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String country;
}
