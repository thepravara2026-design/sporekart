package com.sporekart.content.infrastructure.persistence;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReviewStatus status;

    @Column(name = "moderated_by", length = 100)
    private String moderatedBy;

    @Column(name = "moderated_at")
    private Instant moderatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ReviewEntity() {}

    public ReviewEntity(String id, String productId, String customerId, int rating, String title, String content,
                        ReviewStatus status, String moderatedBy, Instant moderatedAt,
                        Instant createdAt, Instant updatedAt) {
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

    public static ReviewEntity fromDomain(Review review) {
        return new ReviewEntity(
            review.getId(), review.getProductId(), review.getCustomerId(),
            review.getRating(), review.getTitle(), review.getContent(),
            review.getStatus(), review.getModeratedBy(), review.getModeratedAt(),
            review.getCreatedAt(), review.getUpdatedAt());
    }

    public Review toDomain() {
        return new Review(id, productId, customerId, rating, title, content,
            status, moderatedBy, moderatedAt, createdAt, updatedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }
    public String getModeratedBy() { return moderatedBy; }
    public void setModeratedBy(String moderatedBy) { this.moderatedBy = moderatedBy; }
    public Instant getModeratedAt() { return moderatedAt; }
    public void setModeratedAt(Instant moderatedAt) { this.moderatedAt = moderatedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
