package com.example.aiml.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.PasswordResetToken;

public interface PasswordResetTokenRepo
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(Long userId);
}