package com.xor.FAce.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

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

    @ManyToMany
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    protected Set<Authority> authorities = new HashSet<>();

    @Version
    private Short version;

    public User() {
        super();
    }

    public User(String username, String password, String email) {
        this(username, password, email, true, true);
    }

    public User(String username, String password, String email, Boolean verified,
                Boolean loggedIn) {
        this();
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setVerified(verified);
        this.setLoggedIn(loggedIn);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getActive();
    }
}
