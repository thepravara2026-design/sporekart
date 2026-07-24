package com.sporekart.events.domain.content;

import com.sporekart.events.model.DomainEvent;

public class ReviewSubmitted extends DomainEvent {
    private final String reviewId;
    private final String productId;
    private final int rating;

    private ReviewSubmitted(Builder builder) {
        super(builder);
        this.reviewId = builder.reviewId;
        this.productId = builder.productId;
        this.rating = builder.rating;
    }

    public String getReviewId() { return reviewId; }
    public String getProductId() { return productId; }
    public int getRating() { return rating; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String reviewId;
        private String productId;
        private int rating;

        public Builder reviewId(String reviewId) { this.reviewId = reviewId; return this; }
        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder rating(int rating) { this.rating = rating; return this; }

        public ReviewSubmitted build() {
            return new ReviewSubmitted(this);
        }
    }
}
