package com.xor.FAce.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Where(clause = "active = true")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active;

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
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.setActive(true);
    }
}
