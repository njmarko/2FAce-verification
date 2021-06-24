package com.xor.face.repository;

import com.xor.face.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.authorities where u.username=:username and u.active=true and u.verified=true")
    Optional<User> findByUsernameFetchAuthorities(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
