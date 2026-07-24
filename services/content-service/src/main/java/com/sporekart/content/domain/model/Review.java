package com.sporekart.content.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Review {

    private final String id;
    private final String productId;
    private final String customerId;
    private final int rating;
    private final String title;
    private final String content;
    private final ReviewStatus status;
    private final String moderatedBy;
    private final Instant moderatedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Review(String id, String productId, String customerId, int rating, String title, String content,
            ReviewStatus status, String moderatedBy, Instant moderatedAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.status = status;
        this.moderatedBy = moderatedBy;
        this.moderatedAt = moderatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Review create(String productId, String customerId, int rating, String title, String content) {
        return new Review(
                UUID.randomUUID().toString(),
                productId,
                customerId,
                rating,
                title,
                content,
                ReviewStatus.PENDING,
                null,
                null,
                Instant.now(),
                Instant.now());
    }

    public Review withModeration(ReviewStatus newStatus, String moderatorId) {
        return new Review(
                this.id,
                this.productId,
                this.customerId,
                this.rating,
                this.title,
                this.content,
                newStatus,
                moderatorId,
                Instant.now(),
                this.createdAt,
                Instant.now());
    }

    public boolean isModerated() {
        return status != ReviewStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public String getModeratedBy() {
        return moderatedBy;
    }

    public Instant getModeratedAt() {
        return moderatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
