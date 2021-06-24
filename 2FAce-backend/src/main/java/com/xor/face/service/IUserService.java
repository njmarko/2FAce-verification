package com.xor.face.service;

import com.xor.face.domain.entities.User;

public interface IUserService {
    User findByUsernameWithAuthorities(String username);

    User create(User user);
}
