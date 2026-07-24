package com.sporekart.content.infrastructure.persistence;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryContentRepositoryTest {

    private InMemoryContentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryContentRepository();
    }

    @Test
    void saveAndFindByIdRoundTrip() {
        Review review = Review.create("prod-1", "cust-1", 5, "Great", "Excellent");
        Review saved = repository.save(review);

        assertEquals(saved.getId(), repository.findById(saved.getId()).orElseThrow().getId());
    }

    @Test
    void findByProductIdReturnsMatchingReviews() {
        Review r1 = Review.create("prod-1", "cust-1", 5, "A", "Good");
        Review r2 = Review.create("prod-1", "cust-2", 4, "B", "Nice");
        Review r3 = Review.create("prod-2", "cust-1", 3, "C", "Meh");
        repository.save(r1);
        repository.save(r2);
        repository.save(r3);

        List<Review> results = repository.findByProductId("prod-1");

        assertEquals(2, results.size());
    }

    @Test
    void findByCustomerIdReturnsMatchingReviews() {
        Review r1 = Review.create("prod-1", "cust-1", 5, "A", "Good");
        Review r2 = Review.create("prod-2", "cust-1", 4, "B", "Nice");
        Review r3 = Review.create("prod-3", "cust-2", 3, "C", "Meh");
        repository.save(r1);
        repository.save(r2);
        repository.save(r3);

        List<Review> results = repository.findByCustomerId("cust-1");

        assertEquals(2, results.size());
    }

    @Test
    void findByStatusReturnsMatchingReviews() {
        Review r1 = Review.create("prod-1", "cust-1", 5, "A", "Good");
        Review r2 = Review.create("prod-1", "cust-2", 4, "B", "Nice");
        Review approved = r2.withModeration(ReviewStatus.APPROVED, "mod-1");
        Review r3 = Review.create("prod-2", "cust-3", 3, "C", "Meh");
        repository.save(r1);
        repository.save(approved);
        repository.save(r3);

        List<Review> pending = repository.findByStatus(ReviewStatus.PENDING);
        List<Review> resultApproved = repository.findByStatus(ReviewStatus.APPROVED);

        assertEquals(2, pending.size());
        assertEquals(1, resultApproved.size());
    }

    @Test
    void deleteByIdRemovesReview() {
        Review review = Review.create("prod-1", "cust-1", 5, "A", "Good");
        repository.save(review);
        assertTrue(repository.findById(review.getId()).isPresent());

        repository.deleteById(review.getId());

        assertFalse(repository.findById(review.getId()).isPresent());
    }

    @Test
    void findAllReturnsAllReviews() {
        repository.save(Review.create("prod-1", "cust-1", 5, "A", "Good"));
        repository.save(Review.create("prod-2", "cust-2", 4, "B", "Nice"));

        assertEquals(2, repository.findAll().size());
    }
}
