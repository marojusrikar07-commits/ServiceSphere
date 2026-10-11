package com.example.aiml.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aiml.dto.ReviewResponse;
import com.example.aiml.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // CREATE REVIEW
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @RequestParam Long bookingId,
            @RequestParam Long customerId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {

        ReviewResponse response = reviewService.createReview(
                bookingId,
                customerId,
                rating,
                comment
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // GET REVIEW BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    // GET PROVIDER REVIEWS
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ReviewResponse>> getProviderReviews(
            @PathVariable Long providerId) {

        return ResponseEntity.ok(
                reviewService.getProviderReviews(providerId)
        );
    }

    // GET CUSTOMER REVIEWS
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                reviewService.getCustomerReviews(customerId)
        );
    }
}