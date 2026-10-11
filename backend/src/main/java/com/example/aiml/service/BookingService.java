package com.example.aiml.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.dto.BookingResponse;
import com.example.aiml.entity.Booking;
import com.example.aiml.entity.BookingStatus;
import com.example.aiml.entity.Service;
import com.example.aiml.entity.UrgencyLevel;
import com.example.aiml.entity.User;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.provider.ServiceProvider;
import com.example.aiml.entity.ProviderService;
import com.example.aiml.repo.BookingRepo;
import com.example.aiml.repo.ProviderServiceRepo;
import com.example.aiml.repo.ServiceProviderRepo;
import com.example.aiml.repo.ServiceRepo;
import com.example.aiml.repo.UserRepo;

@org.springframework.stereotype.Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final ServiceProviderRepo serviceProviderRepo;
    private final ServiceRepo serviceRepo;
    private final ProviderServiceRepo providerServiceRepo;

    public BookingService(
            BookingRepo bookingRepo,
            UserRepo userRepo,
            ServiceProviderRepo serviceProviderRepo,
            ServiceRepo serviceRepo,
            ProviderServiceRepo providerServiceRepo) {

        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.serviceProviderRepo = serviceProviderRepo;
        this.serviceRepo = serviceRepo;
        this.providerServiceRepo = providerServiceRepo;
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @Transactional
    public BookingResponse createBooking(
            Long customerId,
            Long providerId,
            Long serviceId,
            LocalDate bookingDate,
            LocalTime bookingTime,
            UrgencyLevel urgencyLevel,
            String serviceAddress,
            String serviceCity,
            String serviceArea,
            double serviceLatitude,
            double serviceLongitude,
            String problemDescription,
            String problemPhotoUrl,
            String customerNotes) {

        User customer =
                userRepo.findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found"
                                )
                        );

        if (!customer.getRole().name().equals("CUSTOMER")) {
            throw new IllegalArgumentException(
                    "Only customers can create bookings"
            );
        }

        ServiceProvider provider =
                serviceProviderRepo.findById(providerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service provider not found"
                                )
                        );

        if (!provider.isAvailable()) {
            throw new IllegalArgumentException(
                    "Service provider is currently unavailable"
            );
        }

        Service service =
                serviceRepo.findById(serviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        if (!service.isActive()) {
            throw new IllegalArgumentException(
                    "Service is currently unavailable"
            );
        }

        ProviderService providerService =
                providerServiceRepo
                        .findByProviderIdAndActiveTrue(providerId)
                        .stream()
                        .filter(ps ->
                                ps.getService()
                                        .getId()
                                        .equals(serviceId))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Provider does not offer this service"
                                )
                        );

        if (bookingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Booking date cannot be in the past"
            );
        }

        if (bookingDate.equals(LocalDate.now())
                && bookingTime.isBefore(LocalTime.now())) {

            throw new IllegalArgumentException(
                    "Booking time cannot be in the past"
            );
        }

        List<BookingStatus> activeStatuses = List.of(
                BookingStatus.PENDING,
                BookingStatus.ACCEPTED,
                BookingStatus.IN_PROGRESS
        );

        boolean alreadyBooked =
                bookingRepo
                        .existsByProviderIdAndBookingDateAndBookingTimeAndStatusIn(
                                providerId,
                                bookingDate,
                                bookingTime,
                                activeStatuses
                        );

        if (alreadyBooked) {
            throw new IllegalArgumentException(
                    "Provider is already booked for this date and time"
            );
        }

        double basePrice =
                providerService.getServicePrice();

        double inspectionFee =
                service.getInspectionFee();

        double urgencyFee =
                calculateUrgencyFee(urgencyLevel);

        double estimatedTotal =
                basePrice
                + inspectionFee
                + urgencyFee;

        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setProvider(provider);
        booking.setService(service);

        booking.setBookingDate(bookingDate);
        booking.setBookingTime(bookingTime);

        booking.setUrgencyLevel(urgencyLevel);
        booking.setStatus(BookingStatus.PENDING);

        booking.setServiceAddress(serviceAddress);
        booking.setServiceCity(serviceCity);
        booking.setServiceArea(serviceArea);

        booking.setServiceLatitude(serviceLatitude);
        booking.setServiceLongitude(serviceLongitude);

        booking.setProblemDescription(problemDescription);
        booking.setProblemPhotoUrl(problemPhotoUrl);
        booking.setCustomerNotes(customerNotes);

        booking.setEstimatedBasePrice(basePrice);
        booking.setEstimatedInspectionFee(inspectionFee);
        booking.setUrgencyFee(urgencyFee);
        booking.setEstimatedTotal(estimatedTotal);

        Booking savedBooking =
                bookingRepo.save(booking);

        return BookingResponse.fromBooking(savedBooking);
    }

    // =========================
    // URGENCY FEE
    // =========================

    private double calculateUrgencyFee(
            UrgencyLevel urgencyLevel) {

        return switch (urgencyLevel) {

            case NORMAL -> 0.0;

            case URGENT -> 100.0;

            case EMERGENCY -> 250.0;
        };
    }

    // =========================
    // GET BOOKING
    // =========================

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {

        Booking booking =
                bookingRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        return BookingResponse.fromBooking(booking);
    }

    // =========================
    // CUSTOMER BOOKINGS
    // =========================

    @Transactional(readOnly = true)
    public List<BookingResponse> getCustomerBookings(
            Long customerId) {

        if (!userRepo.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found"
            );
        }

        return bookingRepo
                .findByCustomerIdOrderByBookingDateDescBookingTimeDesc(
                        customerId
                )
                .stream()
                .map(BookingResponse::fromBooking)
                .toList();
    }

    // =========================
    // PROVIDER BOOKINGS
    // =========================

    @Transactional(readOnly = true)
    public List<BookingResponse> getProviderBookings(
            Long providerId) {

        if (!serviceProviderRepo.existsById(providerId)) {
            throw new ResourceNotFoundException(
                    "Service provider not found"
            );
        }

        return bookingRepo
                .findByProviderIdOrderByBookingDateDescBookingTimeDesc(
                        providerId
                )
                .stream()
                .map(BookingResponse::fromBooking)
                .toList();
    }

    // =========================
    // UPDATE BOOKING STATUS
    // =========================

    @Transactional
    public BookingResponse updateBookingStatus(
            Long bookingId,
            BookingStatus newStatus) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        BookingStatus currentStatus =
                booking.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        booking.setStatus(newStatus);

        Booking updatedBooking =
                bookingRepo.save(booking);

        return BookingResponse.fromBooking(
                updatedBooking
        );
    }

    // =========================
    // STATUS VALIDATION
    // =========================

    private void validateStatusTransition(
            BookingStatus currentStatus,
            BookingStatus newStatus) {

        if (currentStatus == BookingStatus.COMPLETED
                || currentStatus == BookingStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Completed or cancelled bookings cannot be changed"
            );
        }

        if (currentStatus == BookingStatus.PENDING) {

            if (newStatus != BookingStatus.ACCEPTED
                    && newStatus != BookingStatus.REJECTED
                    && newStatus != BookingStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Invalid status transition from PENDING"
                );
            }
        }

        else if (currentStatus == BookingStatus.ACCEPTED) {

            if (newStatus != BookingStatus.IN_PROGRESS
                    && newStatus != BookingStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Invalid status transition from ACCEPTED"
                );
            }
        }

        else if (currentStatus == BookingStatus.IN_PROGRESS) {

            if (newStatus != BookingStatus.COMPLETED) {

                throw new IllegalArgumentException(
                        "Only COMPLETED is allowed after IN_PROGRESS"
                );
            }
        }

        else if (currentStatus == BookingStatus.REJECTED) {

            throw new IllegalArgumentException(
                    "Rejected bookings cannot be changed"
            );
        }
    }
}