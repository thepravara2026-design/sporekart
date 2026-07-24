package com.sporekart.events.domain.content;

import com.sporekart.events.model.DomainEvent;

public class ReviewRejected extends DomainEvent {
    private final String reviewId;
    private final String rejectedBy;
    private final String reason;

    private ReviewRejected(Builder builder) {
        super(builder);
        this.reviewId = builder.reviewId;
        this.rejectedBy = builder.rejectedBy;
        this.reason = builder.reason;
    }

    public String getReviewId() { return reviewId; }
    public String getRejectedBy() { return rejectedBy; }
    public String getReason() { return reason; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String reviewId;
        private String rejectedBy;
        private String reason;

        public Builder reviewId(String reviewId) { this.reviewId = reviewId; return this; }
        public Builder rejectedBy(String rejectedBy) { this.rejectedBy = rejectedBy; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }

        public ReviewRejected build() {
            return new ReviewRejected(this);
        }
    }
}
