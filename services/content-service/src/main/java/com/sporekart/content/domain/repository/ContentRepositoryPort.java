package com.sporekart.content.domain.repository;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;

import java.util.List;
import java.util.Optional;

public interface ContentRepositoryPort {

    Review save(Review review);

    Optional<Review> findById(String id);

    List<Review> findAll();

    List<Review> findByProductId(String productId);

    List<Review> findByCustomerId(String customerId);

    List<Review> findByStatus(ReviewStatus status);

    void deleteById(String id);
}
