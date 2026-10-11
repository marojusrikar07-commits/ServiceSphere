package com.example.aiml.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.Review;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    boolean existsByBookingId(Long bookingId);

    Optional<Review> findByBookingId(Long bookingId);

    List<Review> findByProviderIdOrderByCreatedAtDesc(
            Long providerId);

    List<Review> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId);

    List<Review> findByProviderId(Long providerId);

    long countByProviderId(Long providerId);
}