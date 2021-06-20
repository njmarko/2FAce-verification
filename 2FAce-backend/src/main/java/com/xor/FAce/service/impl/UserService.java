package com.xor.FAce.service.impl;

import com.xor.FAce.domain.entities.User;
import com.xor.FAce.domain.exception.EmailAlreadyExistsException;
import com.xor.FAce.domain.exception.UsernameAlreadyExistsException;
import com.xor.FAce.repository.IUserRepository;
import com.xor.FAce.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService, UserDetailsService {
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findByUsernameWithAuthorities(String username) {
        return userRepository.findByUsernameFetchAuthorities(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username: " + username));
    }

    @Override
    @Transactional
    public User create(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new UsernameAlreadyExistsException(u.getUsername());
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException(u.getEmail());
        });
        return userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsernameWithAuthorities(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }
}
