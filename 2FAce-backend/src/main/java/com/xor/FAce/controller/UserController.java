package com.xor.FAce.controller;

import com.xor.FAce.domain.entities.Authority;
import com.xor.FAce.domain.entities.User;
import com.xor.FAce.dto.AuthTokenDTO;
import com.xor.FAce.dto.LoginUserDTO;
import com.xor.FAce.security.util.JwtUtil;
import com.xor.FAce.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(IUserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenDTO createAuthenticationToken(@Valid @RequestBody LoginUserDTO loginUserDTO)  {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authentication);
        String username = jwtUtil.extractUsernameFromToken(token);
        try {
            User user = userService.findByUsernameWithAuthorities(username);
            List<String> authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
            return new AuthTokenDTO(token, username, user.getId(), user.getLoggedIn(),authorities);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }


}
