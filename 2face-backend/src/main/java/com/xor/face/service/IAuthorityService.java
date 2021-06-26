package com.xor.face.service;

import com.xor.face.domain.entities.Authority;

public interface IAuthorityService {
    Authority findAuthority(String name);
}
