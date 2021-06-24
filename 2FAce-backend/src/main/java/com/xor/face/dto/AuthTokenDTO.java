package com.xor.face.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDTO {

    private String jwt;
    private String username;
    private Long id;
    private Boolean loggedIn;
    private List<String> authorities;
}
