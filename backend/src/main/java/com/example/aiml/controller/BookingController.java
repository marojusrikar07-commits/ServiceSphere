package com.example.aiml.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.BookingResponse;
import com.example.aiml.entity.BookingStatus;
import com.example.aiml.entity.UrgencyLevel;
import com.example.aiml.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // =========================
    // CREATE BOOKING
    // =========================

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(

            @RequestParam Long customerId,
            @RequestParam Long providerId,
            @RequestParam Long serviceId,

            @RequestParam LocalDate bookingDate,
            @RequestParam LocalTime bookingTime,

            @RequestParam UrgencyLevel urgencyLevel,

            @RequestParam String serviceAddress,
            @RequestParam String serviceCity,
            @RequestParam String serviceArea,

            @RequestParam double serviceLatitude,
            @RequestParam double serviceLongitude,

            @RequestParam(required = false)
            String problemDescription,

            @RequestParam(required = false)
            String problemPhotoUrl,

            @RequestParam(required = false)
            String customerNotes) {

        BookingResponse response =
                bookingService.createBooking(
                        customerId,
                        providerId,
                        serviceId,
                        bookingDate,
                        bookingTime,
                        urgencyLevel,
                        serviceAddress,
                        serviceCity,
                        serviceArea,
                        serviceLatitude,
                        serviceLongitude,
                        problemDescription,
                        problemPhotoUrl,
                        customerNotes
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET BOOKING BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id)
        );
    }

    // =========================
    // CUSTOMER BOOKINGS
    // =========================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponse>> getCustomerBookings(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                bookingService.getCustomerBookings(
                        customerId
                )
        );
    }

    // =========================
    // PROVIDER BOOKINGS
    // =========================

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<BookingResponse>> getProviderBookings(
            @PathVariable Long providerId) {

        return ResponseEntity.ok(
                bookingService.getProviderBookings(
                        providerId
                )
        );
    }

    // =========================
    // UPDATE BOOKING STATUS
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(

            @PathVariable Long id,

            @RequestParam BookingStatus status) {

        return ResponseEntity.ok(
                bookingService.updateBookingStatus(
                        id,
                        status
                )
        );
    }
}