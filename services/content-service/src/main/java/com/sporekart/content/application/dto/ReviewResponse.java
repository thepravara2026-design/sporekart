package com.sporekart.content.application.dto;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;

import java.time.Instant;

public record ReviewResponse(
        String id,
        String productId,
        String customerId,
        int rating,
        String title,
        String content,
        ReviewStatus status,
        String moderatedBy,
        Instant moderatedAt,
        Instant createdAt,
        Instant updatedAt) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getCustomerId(),
                review.getRating(),
                review.getTitle(),
                review.getContent(),
                review.getStatus(),
                review.getModeratedBy(),
                review.getModeratedAt(),
                review.getCreatedAt(),
                review.getUpdatedAt());
    }
}
