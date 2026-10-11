package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.provider.ServiceProviderResponse;
import com.example.aiml.service.ServiceProviderService;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(
            ServiceProviderService serviceProviderService) {

        this.serviceProviderService = serviceProviderService;
    }

    @PostMapping
    public ResponseEntity<ServiceProviderResponse> createProvider(
            @RequestParam Long userId,
            @RequestParam String businessName,
            @RequestParam String description,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String area,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int experienceYears) {

        ServiceProviderResponse response =
                serviceProviderService.createProvider(
                        userId,
                        businessName,
                        description,
                        address,
                        city,
                        area,
                        latitude,
                        longitude,
                        experienceYears
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderResponse> getProviderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceProviderService.getProviderById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ServiceProviderResponse> getProviderByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                serviceProviderService.getProviderByUserId(userId)
        );
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ServiceProviderResponse>> getProvidersByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                serviceProviderService.getProvidersByCity(city)
        );
    }

    @GetMapping("/city/{city}/available")
    public ResponseEntity<List<ServiceProviderResponse>> getAvailableProvidersByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                serviceProviderService
                        .getAvailableProvidersByCity(city)
        );
    }

    @GetMapping("/verified/available")
    public ResponseEntity<List<ServiceProviderResponse>> getVerifiedAvailableProviders() {

        return ResponseEntity.ok(
                serviceProviderService
                        .getVerifiedAvailableProviders()
        );
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ServiceProviderResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {

        return ResponseEntity.ok(
                serviceProviderService
                        .updateAvailability(id, available)
        );
    }
}