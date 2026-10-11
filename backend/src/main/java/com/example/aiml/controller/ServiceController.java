package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.ServiceResponse;
import com.example.aiml.service.ServiceManagementService;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    public ServiceController(
            ServiceManagementService serviceManagementService) {

        this.serviceManagementService =
                serviceManagementService;
    }

    // =========================
    // CREATE SERVICE
    // =========================

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Long categoryId,
            @RequestParam double basePrice,
            @RequestParam double inspectionFee,
            @RequestParam int estimatedDurationMinutes) {

        ServiceResponse response =
                serviceManagementService.createService(
                        name,
                        description,
                        categoryId,
                        basePrice,
                        inspectionFee,
                        estimatedDurationMinutes
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET SERVICE BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceManagementService
                        .getServiceById(id)
        );
    }

    // =========================
    // GET ALL ACTIVE SERVICES
    // =========================

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getActiveServices() {

        return ResponseEntity.ok(
                serviceManagementService
                        .getActiveServices()
        );
    }

    // =========================
    // GET SERVICES BY CATEGORY
    // =========================

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceResponse>> getServicesByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                serviceManagementService
                        .getServicesByCategory(categoryId)
        );
    }

    // =========================
    // SEARCH SERVICES
    // =========================

    @GetMapping("/search")
    public ResponseEntity<List<ServiceResponse>> searchServices(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                serviceManagementService
                        .searchServices(keyword)
        );
    }

    // =========================
    // UPDATE SERVICE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Long categoryId,
            @RequestParam double basePrice,
            @RequestParam double inspectionFee,
            @RequestParam int estimatedDurationMinutes) {

        return ResponseEntity.ok(
                serviceManagementService.updateService(
                        id,
                        name,
                        description,
                        categoryId,
                        basePrice,
                        inspectionFee,
                        estimatedDurationMinutes
                )
        );
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceResponse> updateServiceStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        return ResponseEntity.ok(
                serviceManagementService
                        .updateServiceStatus(
                                id,
                                active
                        )
        );
    }
}