package com.sporekart.content.infrastructure.persistence;

import com.sporekart.content.domain.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentJpaRepository extends JpaRepository<ReviewEntity, String> {
    List<ReviewEntity> findByProductId(String productId);
    List<ReviewEntity> findByCustomerId(String customerId);
    List<ReviewEntity> findByStatus(ReviewStatus status);
}
