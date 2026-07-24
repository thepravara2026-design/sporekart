package com.sporekart.events.domain.content;

import com.sporekart.events.model.DomainEvent;

public class ReviewApproved extends DomainEvent {
    private final String reviewId;
    private final String approvedBy;

    private ReviewApproved(Builder builder) {
        super(builder);
        this.reviewId = builder.reviewId;
        this.approvedBy = builder.approvedBy;
    }

    public String getReviewId() { return reviewId; }
    public String getApprovedBy() { return approvedBy; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String reviewId;
        private String approvedBy;

        public Builder reviewId(String reviewId) { this.reviewId = reviewId; return this; }
        public Builder approvedBy(String approvedBy) { this.approvedBy = approvedBy; return this; }

        public ReviewApproved build() {
            return new ReviewApproved(this);
        }
    }
}
