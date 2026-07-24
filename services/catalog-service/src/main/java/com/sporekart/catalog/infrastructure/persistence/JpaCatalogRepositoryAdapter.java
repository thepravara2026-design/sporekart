package com.sporekart.catalog.infrastructure.persistence;

import com.sporekart.catalog.domain.model.Product;
import com.sporekart.catalog.domain.repository.ProductRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaCatalogRepositoryAdapter implements ProductRepositoryPort {

    private final CatalogJpaRepository jpaRepository;

    public JpaCatalogRepositoryAdapter(CatalogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(ProductEntity.fromDomain(product)).toDomain();
    }

    @Override
    public Optional<Product> findById(String id) {
        return jpaRepository.findById(id).map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(ProductEntity::toDomain).toList();
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }
}
