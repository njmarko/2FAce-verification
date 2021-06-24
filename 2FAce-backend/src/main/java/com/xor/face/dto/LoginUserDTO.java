package com.xor.face.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUserDTO {

    @NotBlank(message = "Username can't be blank.")
    private String username;

    @NotBlank(message = "Password can't be blank.")
    private String password;
}
