package com.example.aiml.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.dto.ProviderServiceResponse;
import com.example.aiml.entity.ProviderService;
import com.example.aiml.entity.Service;
import com.example.aiml.exception.DuplicateResourceException;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.provider.ServiceProvider;
import com.example.aiml.repo.ProviderServiceRepo;
import com.example.aiml.repo.ServiceRepo;
import com.example.aiml.repo.ServiceProviderRepo;

@org.springframework.stereotype.Service
public class ProviderServiceManagementService {

    private final ProviderServiceRepo providerServiceRepo;
    private final ServiceProviderRepo serviceProviderRepo;
    private final ServiceRepo serviceRepo;

    public ProviderServiceManagementService(
            ProviderServiceRepo providerServiceRepo,
            ServiceProviderRepo serviceProviderRepo,
            ServiceRepo serviceRepo) {

        this.providerServiceRepo = providerServiceRepo;
        this.serviceProviderRepo = serviceProviderRepo;
        this.serviceRepo = serviceRepo;
    }

    // =========================
    // ADD SERVICE TO PROVIDER
    // =========================

    @Transactional
    public ProviderServiceResponse addServiceToProvider(
            Long providerId,
            Long serviceId,
            double servicePrice) {

        ServiceProvider provider =
                serviceProviderRepo.findById(providerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service provider not found"
                                )
                        );

        Service service =
                serviceRepo.findById(serviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        if (!service.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot add an inactive service"
            );
        }

        if (providerServiceRepo
                .existsByProviderIdAndServiceId(
                        providerId,
                        serviceId)) {

            throw new DuplicateResourceException(
                    "Provider already offers this service"
            );
        }

        if (servicePrice < 0) {
            throw new IllegalArgumentException(
                    "Service price cannot be negative"
            );
        }

        ProviderService providerService =
                new ProviderService();

        providerService.setProvider(provider);
        providerService.setService(service);
        providerService.setServicePrice(servicePrice);
        providerService.setActive(true);

        ProviderService savedProviderService =
                providerServiceRepo.save(providerService);

        return ProviderServiceResponse
                .fromProviderService(savedProviderService);
    }

    // =========================
    // GET PROVIDER SERVICES
    // =========================

    @Transactional(readOnly = true)
    public List<ProviderServiceResponse> getProviderServices(
            Long providerId) {

        if (!serviceProviderRepo.existsById(providerId)) {
            throw new ResourceNotFoundException(
                    "Service provider not found"
            );
        }

        return providerServiceRepo
                .findByProviderIdAndActiveTrue(providerId)
                .stream()
                .map(ProviderServiceResponse::fromProviderService)
                .toList();
    }

    // =========================
    // GET PROVIDERS FOR SERVICE
    // =========================

    @Transactional(readOnly = true)
    public List<ProviderServiceResponse> getProvidersForService(
            Long serviceId) {

        if (!serviceRepo.existsById(serviceId)) {
            throw new ResourceNotFoundException(
                    "Service not found"
            );
        }

        return providerServiceRepo
                .findByServiceIdAndActiveTrue(serviceId)
                .stream()
                .map(ProviderServiceResponse::fromProviderService)
                .toList();
    }

    // =========================
    // UPDATE SERVICE PRICE
    // =========================

    @Transactional
    public ProviderServiceResponse updateServicePrice(
            Long providerServiceId,
            double servicePrice) {

        ProviderService providerService =
                providerServiceRepo.findById(providerServiceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider service mapping not found"
                                )
                        );

        if (servicePrice < 0) {
            throw new IllegalArgumentException(
                    "Service price cannot be negative"
            );
        }

        providerService.setServicePrice(servicePrice);

        ProviderService updatedProviderService =
                providerServiceRepo.save(providerService);

        return ProviderServiceResponse
                .fromProviderService(updatedProviderService);
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @Transactional
    public ProviderServiceResponse updateServiceStatus(
            Long providerServiceId,
            boolean active) {

        ProviderService providerService =
                providerServiceRepo.findById(providerServiceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider service mapping not found"
                                )
                        );

        providerService.setActive(active);

        ProviderService updatedProviderService =
                providerServiceRepo.save(providerService);

        return ProviderServiceResponse
                .fromProviderService(updatedProviderService);
    }
}