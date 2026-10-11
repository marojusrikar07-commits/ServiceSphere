package com.example.aiml.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}