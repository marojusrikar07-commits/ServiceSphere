package com.example.aiml.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.provider.ServiceProvider;

public interface ServiceProviderRepo
        extends JpaRepository<ServiceProvider, Long> {

    Optional<ServiceProvider> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    List<ServiceProvider> findByCityIgnoreCase(String city);

    List<ServiceProvider> findByCityIgnoreCaseAndAvailableTrue(
            String city
    );

    List<ServiceProvider> findByVerifiedTrueAndAvailableTrue();
}