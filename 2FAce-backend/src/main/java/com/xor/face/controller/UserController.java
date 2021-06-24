package com.xor.face.controller;

import com.xor.face.domain.entities.Authority;
import com.xor.face.domain.entities.User;
import com.xor.face.dto.AuthTokenDTO;
import com.xor.face.dto.LoginUserDTO;
import com.xor.face.dto.UserDTO;
import com.xor.face.dto.UserRegistrationDTO;
import com.xor.face.security.util.JwtUtil;
import com.xor.face.service.IUserService;
import com.xor.face.support.UserRegistrationDTOToUser;
import com.xor.face.support.UserToUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    private final UserRegistrationDTOToUser toUser;
    private final UserToUserDTO toUserDTO;

    @Autowired
    public UserController(IUserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRegistrationDTOToUser toUser, UserToUserDTO toUserDTO) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        this.toUser = toUser;
        this.toUserDTO = toUserDTO;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        var user = toUser.convert(userRegistrationDTO);
        return toUserDTO.convert(userService.create(user));
    }

}
