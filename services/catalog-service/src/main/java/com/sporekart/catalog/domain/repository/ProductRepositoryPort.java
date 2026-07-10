package com.sporekart.catalog.domain.repository;

import com.sporekart.catalog.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);

    Optional<Product> findById(String id);

    List<Product> findAll();

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);
}
