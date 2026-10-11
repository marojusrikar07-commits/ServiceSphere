package com.example.aiml.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.aiml.provider.ServiceProvider;

import jakarta.persistence.*;

@Entity
@Table(
    name = "bookings",
    indexes = {
        @Index(name = "idx_booking_customer", columnList = "customer_id"),
        @Index(name = "idx_booking_provider", columnList = "provider_id"),
        @Index(name = "idx_booking_service", columnList = "service_id"),
        @Index(name = "idx_booking_status", columnList = "status"),
        @Index(name = "idx_booking_date", columnList = "booking_date")
    }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // CUSTOMER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "customer_id",
        nullable = false
    )
    private User customer;

    // =========================
    // PROVIDER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "provider_id",
        nullable = false
    )
    private ServiceProvider provider;

    // =========================
    // SERVICE
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "service_id",
        nullable = false
    )
    private Service service;

    // =========================
    // BOOKING DETAILS
    // =========================

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "booking_time", nullable = false)
    private LocalTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    // =========================
    // SERVICE ADDRESS
    // =========================

    @Column(nullable = false, length = 500)
    private String serviceAddress;

    @Column(nullable = false, length = 100)
    private String serviceCity;

    @Column(nullable = false, length = 100)
    private String serviceArea;

    @Column(nullable = false)
    private double serviceLatitude;

    @Column(nullable = false)
    private double serviceLongitude;

    // =========================
    // CUSTOMER PROBLEM
    // =========================

    @Column(length = 2000)
    private String problemDescription;

    @Column(length = 500)
    private String problemPhotoUrl;

    // =========================
    // PRICE ESTIMATE
    // =========================

    @Column(nullable = false)
    private double estimatedBasePrice;

    @Column(nullable = false)
    private double estimatedInspectionFee;

    @Column(nullable = false)
    private double urgencyFee;

    @Column(nullable = false)
    private double estimatedTotal;

    // =========================
    // NOTES
    // =========================

    @Column(length = 1000)
    private String customerNotes;

    // =========================
    // TIMESTAMPS
    // =========================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Booking() {
    }

    // =========================
    // LIFECYCLE
    // =========================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public void setProvider(ServiceProvider provider) {
        this.provider = provider;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceCity() {
        return serviceCity;
    }

    public void setServiceCity(String serviceCity) {
        this.serviceCity = serviceCity;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public double getServiceLatitude() {
        return serviceLatitude;
    }

    public void setServiceLatitude(double serviceLatitude) {
        this.serviceLatitude = serviceLatitude;
    }

    public double getServiceLongitude() {
        return serviceLongitude;
    }

    public void setServiceLongitude(double serviceLongitude) {
        this.serviceLongitude = serviceLongitude;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getProblemPhotoUrl() {
        return problemPhotoUrl;
    }

    public void setProblemPhotoUrl(String problemPhotoUrl) {
        this.problemPhotoUrl = problemPhotoUrl;
    }

    public double getEstimatedBasePrice() {
        return estimatedBasePrice;
    }

    public void setEstimatedBasePrice(double estimatedBasePrice) {
        this.estimatedBasePrice = estimatedBasePrice;
    }

    public double getEstimatedInspectionFee() {
        return estimatedInspectionFee;
    }

    public void setEstimatedInspectionFee(
            double estimatedInspectionFee) {

        this.estimatedInspectionFee =
                estimatedInspectionFee;
    }

    public double getUrgencyFee() {
        return urgencyFee;
    }

    public void setUrgencyFee(double urgencyFee) {
        this.urgencyFee = urgencyFee;
    }

    public double getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(double estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}