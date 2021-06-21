package com.xor.FAce.support;

import com.xor.FAce.domain.entities.User;
import com.xor.FAce.dto.UserRegistrationDTO;
import com.xor.FAce.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDTOToUser implements Converter<UserRegistrationDTO, User> {
    private final IAuthorityService authorityService;

    @Autowired
    public UserRegistrationDTOToUser(IAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public User convert(UserRegistrationDTO userRegistrationDTO) {
        var user =  new User(
                        userRegistrationDTO.getUsername(),
                        userRegistrationDTO.getPassword(),
                        userRegistrationDTO.getEmail());
        user.getAuthorities().add(authorityService.findAuthority("ROLE_USER"));
        return user;
    }
}
