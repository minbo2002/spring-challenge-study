package com.example.springchallenge2week.domain.user.repository;

import com.example.springchallenge2week.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNameOrEmail(String name, String email);

    Boolean existsByEmail(String email);
    Boolean existsByName(String name);
}
