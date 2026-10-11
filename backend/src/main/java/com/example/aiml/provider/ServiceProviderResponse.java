package com.example.aiml.provider;

public class ServiceProviderResponse {

    private Long id;
    private Long userId;
    private String name;
    private String businessName;
    private String description;
    private String address;
    private String city;
    private String area;
    private double latitude;
    private double longitude;
    private int experienceYears;
    private boolean available;
    private boolean verified;
    private double averageRating;
    private int totalReviews;

    public ServiceProviderResponse() {
    }

    public ServiceProviderResponse(
            Long id,
            Long userId,
            String name,
            String businessName,
            String description,
            String address,
            String city,
            String area,
            double latitude,
            double longitude,
            int experienceYears,
            boolean available,
            boolean verified,
            double averageRating,
            int totalReviews) {

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.businessName = businessName;
        this.description = description;
        this.address = address;
        this.city = city;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.experienceYears = experienceYears;
        this.available = available;
        this.verified = verified;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }

    public static ServiceProviderResponse fromProvider(
            ServiceProvider provider) {

        return new ServiceProviderResponse(
                provider.getId(),
                provider.getUser().getId(),
                provider.getUser().getName(),
                provider.getBusinessName(),
                provider.getDescription(),
                provider.getAddress(),
                provider.getCity(),
                provider.getArea(),
                provider.getLatitude(),
                provider.getLongitude(),
                provider.getExperienceYears(),
                provider.isAvailable(),
                provider.isVerified(),
                provider.getAverageRating(),
                provider.getTotalReviews()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isVerified() {
        return verified;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }
}