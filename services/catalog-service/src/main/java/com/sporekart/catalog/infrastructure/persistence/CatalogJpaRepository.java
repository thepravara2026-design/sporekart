package com.sporekart.catalog.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogJpaRepository extends JpaRepository<ProductEntity, String> {
    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);
}
