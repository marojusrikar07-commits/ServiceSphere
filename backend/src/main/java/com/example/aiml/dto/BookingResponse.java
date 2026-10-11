package com.example.aiml.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.aiml.entity.Booking;
import com.example.aiml.entity.BookingStatus;
import com.example.aiml.entity.UrgencyLevel;

public class BookingResponse {

    private Long id;

    private Long customerId;
    private String customerName;

    private Long providerId;
    private String providerName;
    private String businessName;

    private Long serviceId;
    private String serviceName;

    private LocalDate bookingDate;
    private LocalTime bookingTime;

    private UrgencyLevel urgencyLevel;
    private BookingStatus status;

    private String serviceAddress;
    private String serviceCity;
    private String serviceArea;

    private double serviceLatitude;
    private double serviceLongitude;

    private String problemDescription;
    private String problemPhotoUrl;
    private String customerNotes;

    private double estimatedBasePrice;
    private double estimatedInspectionFee;
    private double urgencyFee;
    private double estimatedTotal;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookingResponse() {
    }

    public BookingResponse(
            Long id,
            Long customerId,
            String customerName,
            Long providerId,
            String providerName,
            String businessName,
            Long serviceId,
            String serviceName,
            LocalDate bookingDate,
            LocalTime bookingTime,
            UrgencyLevel urgencyLevel,
            BookingStatus status,
            String serviceAddress,
            String serviceCity,
            String serviceArea,
            double serviceLatitude,
            double serviceLongitude,
            String problemDescription,
            String problemPhotoUrl,
            String customerNotes,
            double estimatedBasePrice,
            double estimatedInspectionFee,
            double urgencyFee,
            double estimatedTotal,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.businessName = businessName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.urgencyLevel = urgencyLevel;
        this.status = status;
        this.serviceAddress = serviceAddress;
        this.serviceCity = serviceCity;
        this.serviceArea = serviceArea;
        this.serviceLatitude = serviceLatitude;
        this.serviceLongitude = serviceLongitude;
        this.problemDescription = problemDescription;
        this.problemPhotoUrl = problemPhotoUrl;
        this.customerNotes = customerNotes;
        this.estimatedBasePrice = estimatedBasePrice;
        this.estimatedInspectionFee = estimatedInspectionFee;
        this.urgencyFee = urgencyFee;
        this.estimatedTotal = estimatedTotal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BookingResponse fromBooking(Booking booking) {

        return new BookingResponse(
                booking.getId(),

                booking.getCustomer().getId(),
                booking.getCustomer().getName(),

                booking.getProvider().getId(),
                booking.getProvider().getUser().getName(),
                booking.getProvider().getBusinessName(),

                booking.getService().getId(),
                booking.getService().getName(),

                booking.getBookingDate(),
                booking.getBookingTime(),

                booking.getUrgencyLevel(),
                booking.getStatus(),

                booking.getServiceAddress(),
                booking.getServiceCity(),
                booking.getServiceArea(),

                booking.getServiceLatitude(),
                booking.getServiceLongitude(),

                booking.getProblemDescription(),
                booking.getProblemPhotoUrl(),
                booking.getCustomerNotes(),

                booking.getEstimatedBasePrice(),
                booking.getEstimatedInspectionFee(),
                booking.getUrgencyFee(),
                booking.getEstimatedTotal(),

                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public String getServiceCity() {
        return serviceCity;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public double getServiceLatitude() {
        return serviceLatitude;
    }

    public double getServiceLongitude() {
        return serviceLongitude;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getProblemPhotoUrl() {
        return problemPhotoUrl;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public double getEstimatedBasePrice() {
        return estimatedBasePrice;
    }

    public double getEstimatedInspectionFee() {
        return estimatedInspectionFee;
    }

    public double getUrgencyFee() {
        return urgencyFee;
    }

    public double getEstimatedTotal() {
        return estimatedTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}