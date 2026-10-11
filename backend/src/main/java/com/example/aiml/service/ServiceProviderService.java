package com.example.aiml.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.entity.Role;
import com.example.aiml.entity.User;
import com.example.aiml.exception.DuplicateResourceException;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.provider.ServiceProvider;
import com.example.aiml.provider.ServiceProviderResponse;
import com.example.aiml.repo.ServiceProviderRepo;
import com.example.aiml.repo.UserRepo;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepo serviceProviderRepo;
    private final UserRepo userRepo;

    public ServiceProviderService(
            ServiceProviderRepo serviceProviderRepo,
            UserRepo userRepo) {

        this.serviceProviderRepo = serviceProviderRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public ServiceProviderResponse createProvider(
            Long userId,
            String businessName,
            String description,
            String address,
            String city,
            String area,
            double latitude,
            double longitude,
            int experienceYears) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (user.getRole() != Role.PROVIDER) {
            throw new IllegalArgumentException(
                    "User is not registered as a service provider"
            );
        }

        if (serviceProviderRepo.existsByUserId(userId)) {
            throw new DuplicateResourceException(
                    "Service provider profile already exists"
            );
        }

        if (experienceYears < 0) {
            throw new IllegalArgumentException(
                    "Experience years cannot be negative"
            );
        }

        ServiceProvider provider = new ServiceProvider();

        provider.setUser(user);
        provider.setBusinessName(businessName);
        provider.setDescription(description);
        provider.setAddress(address);
        provider.setCity(city);
        provider.setArea(area);
        provider.setLatitude(latitude);
        provider.setLongitude(longitude);
        provider.setExperienceYears(experienceYears);

        provider.setAvailable(true);
        provider.setVerified(false);
        provider.setAverageRating(0.0);
        provider.setTotalReviews(0);

        ServiceProvider savedProvider =
                serviceProviderRepo.save(provider);

        return ServiceProviderResponse
                .fromProvider(savedProvider);
    }

    @Transactional(readOnly = true)
    public ServiceProviderResponse getProviderById(
            Long id) {

        ServiceProvider provider =
                serviceProviderRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service provider not found"
                                )
                        );

        return ServiceProviderResponse
                .fromProvider(provider);
    }

    @Transactional(readOnly = true)
    public ServiceProviderResponse getProviderByUserId(
            Long userId) {

        ServiceProvider provider =
                serviceProviderRepo.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service provider profile not found"
                                )
                        );

        return ServiceProviderResponse
                .fromProvider(provider);
    }

    @Transactional(readOnly = true)
    public List<ServiceProviderResponse> getProvidersByCity(
            String city) {

        return serviceProviderRepo
                .findByCityIgnoreCase(city)
                .stream()
                .map(ServiceProviderResponse::fromProvider)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceProviderResponse> getAvailableProvidersByCity(
            String city) {

        return serviceProviderRepo
                .findByCityIgnoreCaseAndAvailableTrue(city)
                .stream()
                .map(ServiceProviderResponse::fromProvider)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceProviderResponse> getVerifiedAvailableProviders() {

        return serviceProviderRepo
                .findByVerifiedTrueAndAvailableTrue()
                .stream()
                .map(ServiceProviderResponse::fromProvider)
                .toList();
    }

    @Transactional
    public ServiceProviderResponse updateAvailability(
            Long providerId,
            boolean available) {

        ServiceProvider provider =
                serviceProviderRepo.findById(providerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service provider not found"
                                )
                        );

        provider.setAvailable(available);

        ServiceProvider updatedProvider =
                serviceProviderRepo.save(provider);

        return ServiceProviderResponse
                .fromProvider(updatedProvider);
    }
}