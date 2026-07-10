package com.sporekart.catalog.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Product {
    private final String id;
    private final String sku;
    private final String name;
    private final String slug;
    private final String description;
    private final ProductStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Product(String id, String sku, String name, String slug, String description, ProductStatus status,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(String sku, String name, String slug, String description) {
        return new Product(UUID.randomUUID().toString(), sku, name, slug, description, ProductStatus.DRAFT,
                Instant.now(), Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
