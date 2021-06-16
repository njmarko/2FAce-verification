package com.xor.FAce.domain.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Authority() {
        super();
    }

    public Authority(String name) {
        this.setName(name);
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
