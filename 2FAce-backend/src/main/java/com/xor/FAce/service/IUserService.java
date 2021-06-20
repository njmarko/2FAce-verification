package com.xor.FAce.service;

import com.xor.FAce.domain.entities.User;

public interface IUserService {

    User findByUsernameWithAuthorities(String username);

    User create(User user);
}
