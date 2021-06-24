package com.xor.face.service.impl;

import com.xor.face.domain.entities.Authority;
import com.xor.face.repository.IAuthorityRepository;
import com.xor.face.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
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
