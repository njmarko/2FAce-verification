package com.xor.face;

import com.xor.face.domain.entities.Authority;
import com.xor.face.domain.entities.User;
import com.xor.face.repository.IAuthorityRepository;
import com.xor.face.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final IAuthorityRepository authorityRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(IAuthorityRepository authorityRepository, IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(ApplicationArguments args) {
        var systemAdminAuthority = createAuthority("ROLE_SYSTEM_ADMIN");
        createAuthority("ROLE_USER");

        var user1 = createUser("pera", "pera", "pera@gmail.com");
        user1.getAuthorities().add(systemAdminAuthority);
        userRepository.save(user1);

    }

    private User createUser(String username, String password, String email) {
        return new User(username, passwordEncoder.encode(password), email, true, false);
    }

    private Authority createAuthority(String roleName) {
        var authority = new Authority(roleName);
        authorityRepository.save(authority);
        return authority;
    }

}
