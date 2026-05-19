package com.luxeestates.controller;

import com.luxeestates.model.Review;
import com.luxeestates.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Get all reviews (Admin)
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get approved reviews (Public)
    @GetMapping("/approved")
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatus("Approved");
    }

    // Submit a new review
    @PostMapping
    public Review createReview(@RequestBody Review review) {
        if (review.getName() == null || review.getName().isEmpty()) {
            review.setName("Guest User"); // Default user
        }
        if (review.getProperty() == null || review.getProperty().isEmpty()) {
            review.setProperty("124 Oceanview Drive"); // Default property for demo
        }
        if (review.getStatus() == null) {
            review.setStatus("Pending");
        }
        return reviewRepository.save(review);
    }

    // Update a review (e.g., status, text)
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setStatus(reviewDetails.getStatus());
            review.setComment(reviewDetails.getComment());
            // Can update other fields if needed
            return ResponseEntity.ok(reviewRepository.save(review));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
