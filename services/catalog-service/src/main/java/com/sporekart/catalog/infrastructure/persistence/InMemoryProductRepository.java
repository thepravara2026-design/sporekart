package com.sporekart.catalog.infrastructure.persistence;

import com.sporekart.catalog.domain.model.Product;
import com.sporekart.catalog.domain.repository.ProductRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepositoryPort {
    private final Map<String, Product> productsById = new ConcurrentHashMap<>();
    private final Map<String, String> skuIndex = new ConcurrentHashMap<>();
    private final Map<String, String> slugIndex = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        productsById.put(product.getId(), product);
        skuIndex.put(product.getSku(), product.getId());
        slugIndex.put(product.getSlug(), product.getId());
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(productsById.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productsById.values());
    }

    @Override
    public boolean existsBySku(String sku) {
        return skuIndex.containsKey(sku);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return slugIndex.containsKey(slug);
    }
}
