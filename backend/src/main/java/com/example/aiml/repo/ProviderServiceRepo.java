package com.example.aiml.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.ProviderService;

public interface ProviderServiceRepo
        extends JpaRepository<ProviderService, Long> {

    boolean existsByProviderIdAndServiceId(
            Long providerId,
            Long serviceId
    );

    List<ProviderService> findByProviderIdAndActiveTrue(
            Long providerId
    );

    List<ProviderService> findByServiceIdAndActiveTrue(
            Long serviceId
    );

    List<ProviderService> findByProviderId(
            Long providerId
    );
}