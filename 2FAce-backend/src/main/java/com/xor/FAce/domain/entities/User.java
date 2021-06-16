package com.xor.FAce.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "verified", nullable = false)
    protected Boolean verified;

    @Column(name = "logged_in", nullable = false)
    protected Boolean loggedIn;

    @ManyToMany(cascade = {})
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    protected Set<Authority> authorities = new HashSet<>();

    @Version
    private Short version;

    public User() {
        super();
    }

    protected User(String username, String password, String email, Boolean verified,
                   Boolean loggedIn) {
        this();
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setVerified(verified);
        this.setLoggedIn(loggedIn);
    }
}
