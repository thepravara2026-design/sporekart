package com.sporekart.content.application.service;

import com.sporekart.content.application.dto.CreateReviewRequest;
import com.sporekart.content.application.dto.ReviewResponse;
import com.sporekart.content.common.exception.ReviewNotFoundException;
import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;
import com.sporekart.content.domain.repository.ContentRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepositoryPort contentRepositoryPort;

    public ContentService(ContentRepositoryPort contentRepositoryPort) {
        this.contentRepositoryPort = contentRepositoryPort;
    }

    public ReviewResponse createReview(CreateReviewRequest request) {
        Review review = Review.create(
                request.productId(),
                request.customerId(),
                request.rating(),
                request.title(),
                request.content());
        Review saved = contentRepositoryPort.save(review);
        LOGGER.info("Review created: {}", saved.getId());
        return ReviewResponse.from(saved);
    }

    public ReviewResponse moderateReview(String reviewId, ReviewStatus status, String moderatorId) {
        Review review = contentRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found: " + reviewId));

        if (review.isModerated()) {
            throw new IllegalStateException("Review " + reviewId + " has already been moderated");
        }

        Review moderated = review.withModeration(status, moderatorId);
        Review saved = contentRepositoryPort.save(moderated);
        LOGGER.info("Review moderated: {} -> {}", saved.getId(), status);
        return ReviewResponse.from(saved);
    }

    public List<ReviewResponse> getProductReviews(String productId) {
        List<Review> reviews = contentRepositoryPort.findByProductId(productId).stream()
                .filter(r -> r.getStatus() == ReviewStatus.APPROVED)
                .toList();
        return reviews.stream().map(ReviewResponse::from).toList();
    }

    public List<ReviewResponse> getCustomerReviews(String customerId) {
        return contentRepositoryPort.findByCustomerId(customerId).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    public void deleteReview(String reviewId) {
        Review review = contentRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found: " + reviewId));

        if (review.isModerated()) {
            throw new IllegalStateException("Cannot delete moderated review: " + reviewId);
        }

        contentRepositoryPort.deleteById(reviewId);
        LOGGER.info("Review deleted: {}", reviewId);
    }
}
