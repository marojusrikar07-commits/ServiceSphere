package com.example.aiml.dto;

import com.example.aiml.entity.ProviderService;

public class ProviderServiceResponse {

    private Long id;

    private Long providerId;
    private String providerName;
    private String businessName;

    private Long serviceId;
    private String serviceName;

    private double servicePrice;
    private boolean active;

    public ProviderServiceResponse() {
    }

    public ProviderServiceResponse(
            Long id,
            Long providerId,
            String providerName,
            String businessName,
            Long serviceId,
            String serviceName,
            double servicePrice,
            boolean active) {

        this.id = id;
        this.providerId = providerId;
        this.providerName = providerName;
        this.businessName = businessName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.active = active;
    }

    public static ProviderServiceResponse fromProviderService(
            ProviderService providerService) {

        return new ProviderServiceResponse(
                providerService.getId(),
                providerService.getProvider().getId(),
                providerService.getProvider().getUser().getName(),
                providerService.getProvider().getBusinessName(),
                providerService.getService().getId(),
                providerService.getService().getName(),
                providerService.getServicePrice(),
                providerService.isActive()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public boolean isActive() {
        return active;
    }
}