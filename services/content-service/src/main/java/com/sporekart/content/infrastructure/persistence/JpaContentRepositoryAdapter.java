package com.sporekart.content.infrastructure.persistence;

import com.sporekart.content.domain.model.Review;
import com.sporekart.content.domain.model.ReviewStatus;
import com.sporekart.content.domain.repository.ContentRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaContentRepositoryAdapter implements ContentRepositoryPort {

    private final ContentJpaRepository jpaRepository;

    public JpaContentRepositoryAdapter(ContentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Review save(Review review) {
        return jpaRepository.save(ReviewEntity.fromDomain(review)).toDomain();
    }

    @Override
    public Optional<Review> findById(String id) {
        return jpaRepository.findById(id).map(ReviewEntity::toDomain);
    }

    @Override
    public List<Review> findAll() {
        return jpaRepository.findAll().stream().map(ReviewEntity::toDomain).toList();
    }

    @Override
    public List<Review> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId).stream().map(ReviewEntity::toDomain).toList();
    }

    @Override
    public List<Review> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(ReviewEntity::toDomain).toList();
    }

    @Override
    public List<Review> findByStatus(ReviewStatus status) {
        return jpaRepository.findByStatus(status).stream().map(ReviewEntity::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
