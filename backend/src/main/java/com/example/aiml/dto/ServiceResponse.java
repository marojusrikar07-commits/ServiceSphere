package com.example.aiml.dto;

import com.example.aiml.entity.Service;

public class ServiceResponse {

    private Long id;
    private String name;
    private String description;

    private Long categoryId;
    private String categoryName;
    private String categoryType;

    private double basePrice;
    private double inspectionFee;
    private int estimatedDurationMinutes;

    private boolean active;

    public ServiceResponse() {
    }

    public ServiceResponse(
            Long id,
            String name,
            String description,
            Long categoryId,
            String categoryName,
            String categoryType,
            double basePrice,
            double inspectionFee,
            int estimatedDurationMinutes,
            boolean active) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.basePrice = basePrice;
        this.inspectionFee = inspectionFee;
        this.estimatedDurationMinutes =
                estimatedDurationMinutes;
        this.active = active;
    }

    public static ServiceResponse fromService(
            Service service) {

        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getCategory().getId(),
                service.getCategory().getName(),
                service.getCategory().getCategoryType(),
                service.getBasePrice(),
                service.getInspectionFee(),
                service.getEstimatedDurationMinutes(),
                service.isActive()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getInspectionFee() {
        return inspectionFee;
    }

    public int getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public boolean isActive() {
        return active;
    }
}