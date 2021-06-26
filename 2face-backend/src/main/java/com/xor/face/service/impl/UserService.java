package com.xor.face.service.impl;

import com.xor.face.domain.entities.User;
import com.xor.face.domain.exceptions.EmailAlreadyExistsException;
import com.xor.face.domain.exceptions.NoAuthorityException;
import com.xor.face.domain.exceptions.UsernameAlreadyExistsException;
import com.xor.face.repository.IUserRepository;
import com.xor.face.service.IUserService;
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
        throwIfUsernameExists(user);
        throwIfEmailExists(user);
        throwIfNoAuthorities(user);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsernameWithAuthorities(username);
    }

    private void throwIfNoAuthorities(User user) {
        if (user.getAuthorities().isEmpty()) {
            throw new NoAuthorityException();
        }
    }

    private void throwIfEmailExists(User user) {
        var userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent()) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }
    }

    private void throwIfUsernameExists(User user) {
        var userWithSameUsername = userRepository.findByUsername(user.getUsername());
        if (userWithSameUsername.isPresent()) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }
}
