package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.LoginRequest;
import com.example.aiml.dto.ProviderRegisterRequest;
import com.example.aiml.dto.RegisterRequest;
import com.example.aiml.dto.UserResponse;
import com.example.aiml.entity.User;
import com.example.aiml.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

        User savedUser =
                userService.registerProvider(request);

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