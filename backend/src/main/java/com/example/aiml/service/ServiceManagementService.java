package com.example.aiml.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.dto.ServiceResponse;
import com.example.aiml.entity.ServiceCategory;
import com.example.aiml.exception.DuplicateResourceException;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.repo.ServiceCategoryRepo;
import com.example.aiml.repo.ServiceRepo;

@org.springframework.stereotype.Service
public class ServiceManagementService {

    private final ServiceRepo serviceRepo;
    private final ServiceCategoryRepo serviceCategoryRepo;

    public ServiceManagementService(
            ServiceRepo serviceRepo,
            ServiceCategoryRepo serviceCategoryRepo) {

        this.serviceRepo = serviceRepo;
        this.serviceCategoryRepo = serviceCategoryRepo;
    }

    // =========================
    // CREATE SERVICE
    // =========================

    @Transactional
    public ServiceResponse createService(
            String name,
            String description,
            Long categoryId,
            double basePrice,
            double inspectionFee,
            int estimatedDurationMinutes) {

        ServiceCategory category =
                serviceCategoryRepo.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service category not found"
                                )
                        );

        if (!category.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot create service under an inactive category"
            );
        }

        if (serviceRepo.existsByNameIgnoreCaseAndCategoryId(
                name,
                categoryId)) {

            throw new DuplicateResourceException(
                    "Service already exists in this category"
            );
        }

        if (basePrice < 0) {
            throw new IllegalArgumentException(
                    "Base price cannot be negative"
            );
        }

        if (inspectionFee < 0) {
            throw new IllegalArgumentException(
                    "Inspection fee cannot be negative"
            );
        }

        if (estimatedDurationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Estimated duration must be greater than zero"
            );
        }

        com.example.aiml.entity.Service service =
                new com.example.aiml.entity.Service();

        service.setName(name);
        service.setDescription(description);
        service.setCategory(category);
        service.setBasePrice(basePrice);
        service.setInspectionFee(inspectionFee);
        service.setEstimatedDurationMinutes(
                estimatedDurationMinutes
        );
        service.setActive(true);

        com.example.aiml.entity.Service savedService =
                serviceRepo.save(service);

        return ServiceResponse.fromService(savedService);
    }

    // =========================
    // GET SERVICE BY ID
    // =========================

    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {

        com.example.aiml.entity.Service service =
                serviceRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        return ServiceResponse.fromService(service);
    }

    // =========================
    // GET ALL ACTIVE SERVICES
    // =========================

    @Transactional(readOnly = true)
    public List<ServiceResponse> getActiveServices() {

        return serviceRepo.findByActiveTrue()
                .stream()
                .map(ServiceResponse::fromService)
                .toList();
    }

    // =========================
    // GET SERVICES BY CATEGORY
    // =========================

    @Transactional(readOnly = true)
    public List<ServiceResponse> getServicesByCategory(
            Long categoryId) {

        return serviceRepo
                .findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(ServiceResponse::fromService)
                .toList();
    }

    // =========================
    // SEARCH SERVICES
    // =========================

    @Transactional(readOnly = true)
    public List<ServiceResponse> searchServices(
            String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getActiveServices();
        }

        return serviceRepo
                .findByNameContainingIgnoreCaseAndActiveTrue(
                        keyword.trim()
                )
                .stream()
                .map(ServiceResponse::fromService)
                .toList();
    }

    // =========================
    // UPDATE SERVICE
    // =========================

    @Transactional
    public ServiceResponse updateService(
            Long id,
            String name,
            String description,
            Long categoryId,
            double basePrice,
            double inspectionFee,
            int estimatedDurationMinutes) {

        com.example.aiml.entity.Service service =
                serviceRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        ServiceCategory category =
                serviceCategoryRepo.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service category not found"
                                )
                        );

        if (!category.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot assign service to an inactive category"
            );
        }

        if ((!service.getName().equalsIgnoreCase(name)
                || !service.getCategory().getId().equals(categoryId))
                && serviceRepo
                        .existsByNameIgnoreCaseAndCategoryId(
                                name,
                                categoryId)) {

            throw new DuplicateResourceException(
                    "Another service with this name already exists in the category"
            );
        }

        if (basePrice < 0) {
            throw new IllegalArgumentException(
                    "Base price cannot be negative"
            );
        }

        if (inspectionFee < 0) {
            throw new IllegalArgumentException(
                    "Inspection fee cannot be negative"
            );
        }

        if (estimatedDurationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Estimated duration must be greater than zero"
            );
        }

        service.setName(name);
        service.setDescription(description);
        service.setCategory(category);
        service.setBasePrice(basePrice);
        service.setInspectionFee(inspectionFee);
        service.setEstimatedDurationMinutes(
                estimatedDurationMinutes
        );

        com.example.aiml.entity.Service updatedService =
                serviceRepo.save(service);

        return ServiceResponse.fromService(updatedService);
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @Transactional
    public ServiceResponse updateServiceStatus(
            Long id,
            boolean active) {

        com.example.aiml.entity.Service service =
                serviceRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        service.setActive(active);

        com.example.aiml.entity.Service updatedService =
                serviceRepo.save(service);

        return ServiceResponse.fromService(updatedService);
    }
}