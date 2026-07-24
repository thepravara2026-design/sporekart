package com.sporekart.content.infrastructure.persistence;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;
import com.sporekart.content.domain.repository.ContentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryContentRepository implements ContentRepositoryPort {

    private final Map<String, Review> reviewsById = new ConcurrentHashMap<>();

    @Override
    public Review save(Review review) {
        reviewsById.put(review.getId(), review);
        return review;
    }

    @Override
    public Optional<Review> findById(String id) {
        return Optional.ofNullable(reviewsById.get(id));
    }

    @Override
    public List<Review> findAll() {
        return new ArrayList<>(reviewsById.values());
    }

    @Override
    public List<Review> findByProductId(String productId) {
        return reviewsById.values().stream()
                .filter(review -> review.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByCustomerId(String customerId) {
        return reviewsById.values().stream()
                .filter(review -> review.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByStatus(ReviewStatus status) {
        return reviewsById.values().stream()
                .filter(review -> review.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        reviewsById.remove(id);
    }
}
