package com.example.aiml.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aiml.entity.Booking;
import com.example.aiml.entity.BookingStatus;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByBookingDateDescBookingTimeDesc(
            Long customerId
    );

    List<Booking> findByProviderIdOrderByBookingDateDescBookingTimeDesc(
            Long providerId
    );

    List<Booking> findByCustomerIdAndStatus(
            Long customerId,
            BookingStatus status
    );

    List<Booking> findByProviderIdAndStatus(
            Long providerId,
            BookingStatus status
    );

    List<Booking> findByProviderIdAndBookingDate(
            Long providerId,
            LocalDate bookingDate
    );

    boolean existsByProviderIdAndBookingDateAndBookingTimeAndStatusIn(
            Long providerId,
            LocalDate bookingDate,
            java.time.LocalTime bookingTime,
            List<BookingStatus> statuses
    );
}