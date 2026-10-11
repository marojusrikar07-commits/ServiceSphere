package com.example.aiml.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.Service;

public interface ServiceRepo extends JpaRepository<Service, Long> {

    List<Service> findByActiveTrue();

    List<Service> findByCategoryId(Long categoryId);

    List<Service> findByCategoryIdAndActiveTrue(
            Long categoryId
    );

    boolean existsByNameIgnoreCaseAndCategoryId(
            String name,
            Long categoryId
    );

    List<Service> findByNameContainingIgnoreCaseAndActiveTrue(
            String name
    );
}