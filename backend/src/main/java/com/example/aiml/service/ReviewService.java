package com.example.aiml.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.aiml.dto.ReviewResponse;
import com.example.aiml.entity.Booking;
import com.example.aiml.entity.BookingStatus;
import com.example.aiml.entity.Review;
import com.example.aiml.exception.ResourceNotFoundException;
import com.example.aiml.provider.ServiceProvider;
import com.example.aiml.repo.BookingRepo;
import com.example.aiml.repo.ReviewRepo;
import com.example.aiml.repo.ServiceProviderRepo;

@org.springframework.stereotype.Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final BookingRepo bookingRepo;
    private final ServiceProviderRepo serviceProviderRepo;

    public ReviewService(
            ReviewRepo reviewRepo,
            BookingRepo bookingRepo,
            ServiceProviderRepo serviceProviderRepo) {

        this.reviewRepo = reviewRepo;
        this.bookingRepo = bookingRepo;
        this.serviceProviderRepo = serviceProviderRepo;
    }

    // CREATE REVIEW
    @Transactional
    public ReviewResponse createReview(
            Long bookingId,
            Long customerId,
            int rating,
            String comment) {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5"
            );
        }

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"
                        )
                );

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException(
                    "You can review only your own bookings"
            );
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Only completed bookings can be reviewed"
            );
        }

        if (reviewRepo.existsByBookingId(bookingId)) {
            throw new IllegalArgumentException(
                    "This booking has already been reviewed"
            );
        }

        Review review = new Review();

        review.setBooking(booking);
        review.setCustomer(booking.getCustomer());
        review.setProvider(booking.getProvider());
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepo.save(review);

        updateProviderRating(booking.getProvider());

        return ReviewResponse.fromReview(savedReview);
    }

    // GET REVIEW BY ID
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found"
                        )
                );

        return ReviewResponse.fromReview(review);
    }

    // GET REVIEWS FOR A PROVIDER
    @Transactional(readOnly = true)
    public List<ReviewResponse> getProviderReviews(
            Long providerId) {

        if (!serviceProviderRepo.existsById(providerId)) {
            throw new ResourceNotFoundException(
                    "Service provider not found"
            );
        }

        return reviewRepo
                .findByProviderIdOrderByCreatedAtDesc(providerId)
                .stream()
                .map(ReviewResponse::fromReview)
                .toList();
    }

    // GET REVIEWS BY A CUSTOMER
    @Transactional(readOnly = true)
    public List<ReviewResponse> getCustomerReviews(
            Long customerId) {

        return reviewRepo
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(ReviewResponse::fromReview)
                .toList();
    }

    // UPDATE PROVIDER'S AVERAGE RATING
    private void updateProviderRating(
            ServiceProvider provider) {

        List<Review> reviews =
                reviewRepo.findByProviderId(provider.getId());

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        provider.setAverageRating(
                Math.round(average * 100.0) / 100.0
        );

        provider.setTotalReviews(reviews.size());

        serviceProviderRepo.save(provider);
    }
}