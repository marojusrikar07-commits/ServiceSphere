package com.example.aiml.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.aiml.dto.LoginRequest;
import com.example.aiml.dto.ProviderRegisterRequest;
import com.example.aiml.dto.RegisterRequest;
import com.example.aiml.entity.Role;
import com.example.aiml.entity.User;
import com.example.aiml.exception.DuplicateResourceException;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.repo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // CUSTOMER REGISTRATION
    // =========================

    public User registerUser(RegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.CUSTOMER);

        return userRepo.save(user);
    }

    // =========================
    // PROVIDER REGISTRATION
    // =========================

    public User registerProvider(
            ProviderRegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.PROVIDER);

        return userRepo.save(user);
    }

    // =========================
    // LOGIN
    // =========================

    public User loginUser(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid email or password"
                        )
                );

        if (!user.isActive()) {
            throw new ResourceNotFoundException(
                    "Account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new ResourceNotFoundException(
                    "Invalid email or password"
            );
        }

        return user;
    }

    // =========================
    // GET ALL USERS
    // =========================

    public List<User> getAllUsers() {

        return userRepo.findAll();
    }

    // =========================
    // GET USER BY ID
    // =========================

    public User getUserById(Long id) {

        return userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    // =========================
    // DELETE USER
    // =========================

    public void deleteUser(Long id) {

        if (!userRepo.existsById(id)) {

            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        userRepo.deleteById(id);
    }
}