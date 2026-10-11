package com.example.aiml.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.ServiceCategory;

public interface ServiceCategoryRepo
        extends JpaRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<ServiceCategory> findByActiveTrue();

    List<ServiceCategory> findByCategoryTypeIgnoreCase(
            String categoryType
    );

    List<ServiceCategory> findByCategoryTypeIgnoreCaseAndActiveTrue(
            String categoryType
    );
}