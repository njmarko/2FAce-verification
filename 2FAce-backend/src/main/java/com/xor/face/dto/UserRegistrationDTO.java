package com.xor.face.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Username can't be empty.")
    private String username;

    @NotBlank(message = "Email can't be empty.")
    @Email(message = "Email is not in valid format.")
    private String email;

    @NotBlank(message = "Password can't be empty.")
    private String password;

}
