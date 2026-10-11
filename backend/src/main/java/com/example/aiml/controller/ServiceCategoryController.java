package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.entity.ServiceCategory;
import com.example.aiml.service.ServiceCategoryService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceCategoryController {

    private final ServiceCategoryService serviceCategoryService;

    public ServiceCategoryController(
            ServiceCategoryService serviceCategoryService) {

        this.serviceCategoryService = serviceCategoryService;
    }

    // =========================
    // CREATE CATEGORY
    // =========================

    @PostMapping
    public ResponseEntity<ServiceCategory> createCategory(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String categoryType) {

        ServiceCategory category =
                serviceCategoryService.createCategory(
                        name,
                        description,
                        categoryType
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(category);
    }

    // =========================
    // GET CATEGORY BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceCategoryService.getCategoryById(id)
        );
    }

    // =========================
    // GET ALL ACTIVE CATEGORIES
    // =========================

    @GetMapping
    public ResponseEntity<List<ServiceCategory>> getActiveCategories() {

        return ResponseEntity.ok(
                serviceCategoryService.getActiveCategories()
        );
    }

    // =========================
    // GET CATEGORIES BY TYPE
    // =========================

    @GetMapping("/type/{categoryType}")
    public ResponseEntity<List<ServiceCategory>> getCategoriesByType(
            @PathVariable String categoryType) {

        return ResponseEntity.ok(
                serviceCategoryService
                        .getCategoriesByType(categoryType)
        );
    }

    // =========================
    // UPDATE CATEGORY
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String categoryType) {

        return ResponseEntity.ok(
                serviceCategoryService.updateCategory(
                        id,
                        name,
                        description,
                        categoryType
                )
        );
    }

    // =========================
    // ACTIVATE / DEACTIVATE
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceCategory> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        return ResponseEntity.ok(
                serviceCategoryService
                        .updateCategoryStatus(id, active)
        );
    }
}