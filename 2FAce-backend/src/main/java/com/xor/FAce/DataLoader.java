package com.xor.FAce;

import com.xor.FAce.domain.entities.Authority;
import com.xor.FAce.domain.entities.User;
import com.xor.FAce.repository.IAuthorityRepository;
import com.xor.FAce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final IAuthorityRepository authorityRepository;
    private final IUserRepository userRepository;

    @Autowired
    public DataLoader(IAuthorityRepository authorityRepository, IUserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Authority systemAdminAuthority = createAuthority("ROLE_SYSTEM_ADMIN");

        User user1 = new User("pera", "pera", "pera@gmail.com", true, false);
        user1.getAuthorities().add(systemAdminAuthority);
        userRepository.save(user1);

    }

    private Authority createAuthority(String roleName) {
        Authority authority = new Authority(roleName);
        authorityRepository.save(authority);
        return authority;
    }

}
