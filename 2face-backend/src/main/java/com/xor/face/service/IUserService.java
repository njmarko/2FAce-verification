package com.xor.face.service;

import com.xor.face.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User findByUsernameWithAuthorities(String username);

    User create(User user);
}
