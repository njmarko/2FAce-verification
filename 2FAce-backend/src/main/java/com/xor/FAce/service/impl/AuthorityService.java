package com.xor.FAce.service.impl;

import com.xor.FAce.domain.entities.Authority;
import com.xor.FAce.repository.IAuthorityRepository;
import com.xor.FAce.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService implements IAuthorityService {
    private final IAuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(IAuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findAuthority(String name) {
        return this.authorityRepository.findByName(name);
    }
}
