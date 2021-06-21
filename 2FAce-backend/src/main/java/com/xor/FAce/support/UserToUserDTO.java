package com.xor.FAce.support;

import com.xor.FAce.domain.entities.User;
import com.xor.FAce.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTO implements Converter<User, UserDTO> {
    private final ModelMapper mapper;

    @Autowired
    public UserToUserDTO(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO convert(@NonNull User user) {
        return mapper.map(user, UserDTO.class);
    }
}
