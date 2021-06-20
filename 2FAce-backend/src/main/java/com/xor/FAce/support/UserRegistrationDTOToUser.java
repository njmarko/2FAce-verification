package com.xor.FAce.support;

import com.xor.FAce.domain.entities.User;
import com.xor.FAce.dto.UserRegistrationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDTOToUser implements Converter<UserRegistrationDTO, User> {
    @Override
    public User convert(UserRegistrationDTO userRegistrationDTO) {
        return new User(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword(),
                userRegistrationDTO.getEmail());
    }
}
