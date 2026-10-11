package com.example.aiml.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.entity.PasswordResetToken;
import com.example.aiml.entity.User;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.repo.PasswordResetTokenRepo;
import com.example.aiml.repo.UserRepo;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepo tokenRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.password-reset.dev-mode:false}")
    private boolean developmentMode;

    public PasswordResetService(
            PasswordResetTokenRepo tokenRepo,
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {

        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String requestReset(String email) {

        userRepo.findByEmail(email.trim().toLowerCase())
                .ifPresent(user -> {

                    tokenRepo.deleteByUserId(user.getId());

                    byte[] randomBytes = new byte[32];
                    secureRandom.nextBytes(randomBytes);

                    String token = Base64.getUrlEncoder()
                            .withoutPadding()
                            .encodeToString(randomBytes);

                    PasswordResetToken resetToken =
                            new PasswordResetToken(
                                    token,
                                    user,
                                    LocalDateTime.now().plusMinutes(15)
                            );

                    tokenRepo.save(resetToken);

                    if (developmentMode) {
                        System.out.println(
                                "[DEV ONLY] Password reset link for "
                                + user.getEmail()
                                + ": http://localhost:5173/reset-password?token="
                                + token
                        );
                    }
                });

        // Same response whether or not the email exists.
        return "If an account exists for this email, password reset instructions will be available.";
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                tokenRepo.findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invalid or expired reset token"
                                )
                        );

        if (resetToken.isUsed()
                || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new ResourceNotFoundException(
                    "Invalid or expired reset token"
            );
        }

        User user = resetToken.getUser();

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        userRepo.save(user);

        resetToken.setUsed(true);
        tokenRepo.save(resetToken);
    }
}