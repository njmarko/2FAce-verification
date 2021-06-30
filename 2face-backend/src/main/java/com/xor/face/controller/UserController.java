package com.xor.face.controller;

import com.xor.face.domain.entities.Authority;
import com.xor.face.dto.request.LoginUserDTO;
import com.xor.face.dto.request.UserRegistrationDTO;
import com.xor.face.dto.response.AuthTokenDTO;
import com.xor.face.dto.response.UserDTO;
import com.xor.face.security.authentication.FaceVerificationAuthenticationToken;
import com.xor.face.security.util.JwtUtil;
import com.xor.face.service.IUserService;
import com.xor.face.support.UserRegistrationDTOToUser;
import com.xor.face.support.UserToUserDTO;
import com.xor.face.twofa.core.credentials.login.impl.LoginCredentials;
import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IFaceVerification2FA faceVerificationProvider;

    private final UserRegistrationDTOToUser toUser;
    private final UserToUserDTO toUserDTO;

    @Autowired
    public UserController(IUserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, IFaceVerification2FA faceVerificationProvider, UserRegistrationDTOToUser toUser, UserToUserDTO toUserDTO) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.faceVerificationProvider = faceVerificationProvider;

        this.toUser = toUser;
        this.toUserDTO = toUserDTO;
    }

    @GetMapping(value = "/2fa")
    public boolean getCurrent2FAProvider() {
        var result = faceVerificationProvider.isValid(new LoginCredentials("dusan", "neka slika"));
        return result.isVerificationSuccessful();
    }


    @PostMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenDTO createAuthenticationToken(@Valid @RequestBody LoginUserDTO loginUserDTO)  {
        var authentication = authenticationManager.authenticate(
                new FaceVerificationAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword(), loginUserDTO.getImage())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authentication);
        String username = jwtUtil.extractUsernameFromToken(token);
        var user = userService.findByUsernameWithAuthorities(username);
        List<String> authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        return new AuthTokenDTO(token, username, user.getId(), user.getLoggedIn(),authorities);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        var user = toUser.convert(userRegistrationDTO);
        var userDto =  toUserDTO.convert(userService.create(user));
        if(!faceVerificationProvider.register(userRegistrationDTO).isRegistrationSuccessful()){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Registration was unsuccessful");
        }
        return userDto;
    }

}
