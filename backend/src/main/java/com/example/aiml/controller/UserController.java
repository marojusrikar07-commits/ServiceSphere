package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.LoginRequest;
import com.example.aiml.dto.ProviderRegisterRequest;
import com.example.aiml.dto.RegisterRequest;
import com.example.aiml.dto.UserResponse;
import com.example.aiml.dto.ForgotPasswordRequest;
import com.example.aiml.dto.ResetPasswordRequest;

import com.example.aiml.entity.User;
import com.example.aiml.service.UserService;
import com.example.aiml.service.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public UserController(
            UserService userService,
            PasswordResetService passwordResetService) {

        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    // =========================
    // CUSTOMER REGISTRATION
    // =========================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        User savedUser = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.fromUser(savedUser));
    }

    // =========================
    // PROVIDER REGISTRATION
    // =========================

    @PostMapping("/provider/register")
    public ResponseEntity<UserResponse> registerProvider(
            @Valid @RequestBody ProviderRegisterRequest request) {

        User savedUser = userService.registerProvider(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.fromUser(savedUser));
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.loginUser(request);

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    // =========================
    // FORGOT PASSWORD
    // =========================

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        String message = passwordResetService.requestReset(
                request.getEmail()
        );

        return ResponseEntity.ok(message);
    }

    // =========================
    // RESET PASSWORD
    // =========================

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                "Password reset successfully. You can now log in."
        );
    }

    // =========================
    // GET ALL USERS
    // =========================

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // =========================
    // GET USER BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // =========================
    // DELETE USER
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}