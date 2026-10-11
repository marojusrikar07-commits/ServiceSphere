package com.example.aiml.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.entity.ServiceCategory;
import com.example.aiml.exception.DuplicateResourceException;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.repo.ServiceCategoryRepo;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepo serviceCategoryRepo;

    public ServiceCategoryService(
            ServiceCategoryRepo serviceCategoryRepo) {

        this.serviceCategoryRepo = serviceCategoryRepo;
    }

    // =========================
    // CREATE CATEGORY
    // =========================

    @Transactional
    public ServiceCategory createCategory(
            String name,
            String description,
            String categoryType) {

        if (serviceCategoryRepo.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException(
                    "Service category already exists"
            );
        }

        ServiceCategory category = new ServiceCategory();

        category.setName(name);
        category.setDescription(description);
        category.setCategoryType(categoryType.toUpperCase());
        category.setActive(true);

        return serviceCategoryRepo.save(category);
    }

    // =========================
    // GET CATEGORY BY ID
    // =========================

    @Transactional(readOnly = true)
    public ServiceCategory getCategoryById(Long id) {

        return serviceCategoryRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service category not found"
                        )
                );
    }

    // =========================
    // GET ALL ACTIVE CATEGORIES
    // =========================

    @Transactional(readOnly = true)
    public List<ServiceCategory> getActiveCategories() {

        return serviceCategoryRepo.findByActiveTrue();
    }

    // =========================
    // GET CATEGORIES BY TYPE
    // =========================

    @Transactional(readOnly = true)
    public List<ServiceCategory> getCategoriesByType(
            String categoryType) {

        return serviceCategoryRepo
                .findByCategoryTypeIgnoreCaseAndActiveTrue(
                        categoryType
                );
    }

    // =========================
    // UPDATE CATEGORY
    // =========================

    @Transactional
    public ServiceCategory updateCategory(
            Long id,
            String name,
            String description,
            String categoryType) {

        ServiceCategory category =
                serviceCategoryRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service category not found"
                                )
                        );

        if (!category.getName().equalsIgnoreCase(name)
                && serviceCategoryRepo.existsByNameIgnoreCase(name)) {

            throw new DuplicateResourceException(
                    "Another service category with this name already exists"
            );
        }

        category.setName(name);
        category.setDescription(description);
        category.setCategoryType(
                categoryType.toUpperCase()
        );

        return serviceCategoryRepo.save(category);
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @Transactional
    public ServiceCategory updateCategoryStatus(
            Long id,
            boolean active) {

        ServiceCategory category =
                serviceCategoryRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service category not found"
                                )
                        );

        category.setActive(active);

        return serviceCategoryRepo.save(category);
    }
}