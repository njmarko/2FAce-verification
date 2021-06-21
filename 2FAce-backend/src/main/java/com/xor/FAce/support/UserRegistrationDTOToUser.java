package com.xor.FAce.support;

import com.xor.FAce.domain.entities.User;
import com.xor.FAce.dto.UserRegistrationDTO;
import com.xor.FAce.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDTOToUser implements Converter<UserRegistrationDTO, User> {
    private final IAuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationDTOToUser(IAuthorityService authorityService, PasswordEncoder passwordEncoder) {
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User convert(UserRegistrationDTO userRegistrationDTO) {
        var user =  new User(
                        userRegistrationDTO.getUsername(),
                        passwordEncoder.encode(userRegistrationDTO.getPassword()),
                        userRegistrationDTO.getEmail());
        user.getAuthorities().add(authorityService.findAuthority("ROLE_USER"));
        return user;
    }
}
