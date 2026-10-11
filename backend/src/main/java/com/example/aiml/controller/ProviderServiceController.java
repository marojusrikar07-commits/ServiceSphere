package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.ProviderServiceResponse;
import com.example.aiml.service.ProviderServiceManagementService;

@RestController
@RequestMapping("/api/provider-services")
@CrossOrigin(origins = "http://localhost:5173")
public class ProviderServiceController {

    private final ProviderServiceManagementService
            providerServiceManagementService;

    public ProviderServiceController(
            ProviderServiceManagementService
                    providerServiceManagementService) {

        this.providerServiceManagementService =
                providerServiceManagementService;
    }

    // =========================
    // ADD SERVICE TO PROVIDER
    // =========================

    @PostMapping
    public ResponseEntity<ProviderServiceResponse>
            addServiceToProvider(

            @RequestParam Long providerId,
            @RequestParam Long serviceId,
            @RequestParam double servicePrice) {

        ProviderServiceResponse response =
                providerServiceManagementService
                        .addServiceToProvider(
                                providerId,
                                serviceId,
                                servicePrice
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET PROVIDER SERVICES
    // =========================

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ProviderServiceResponse>>
            getProviderServices(
                    @PathVariable Long providerId) {

        return ResponseEntity.ok(
                providerServiceManagementService
                        .getProviderServices(providerId)
        );
    }

    // =========================
    // GET PROVIDERS FOR SERVICE
    // =========================

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ProviderServiceResponse>>
            getProvidersForService(
                    @PathVariable Long serviceId) {

        return ResponseEntity.ok(
                providerServiceManagementService
                        .getProvidersForService(serviceId)
        );
    }

    // =========================
    // UPDATE SERVICE PRICE
    // =========================

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProviderServiceResponse>
            updateServicePrice(
                    @PathVariable Long id,
                    @RequestParam double servicePrice) {

        return ResponseEntity.ok(
                providerServiceManagementService
                        .updateServicePrice(
                                id,
                                servicePrice
                        )
        );
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProviderServiceResponse>
            updateServiceStatus(
                    @PathVariable Long id,
                    @RequestParam boolean active) {

        return ResponseEntity.ok(
                providerServiceManagementService
                        .updateServiceStatus(
                                id,
                                active
                        )
        );
    }
}