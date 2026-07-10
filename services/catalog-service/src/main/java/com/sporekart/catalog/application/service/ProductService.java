package com.sporekart.catalog.application.service;

import com.sporekart.catalog.application.dto.CreateProductRequest;
import com.sporekart.catalog.domain.model.Product;
import com.sporekart.catalog.domain.repository.ProductRepositoryPort;
import com.sporekart.catalog.common.exception.DuplicateSkuException;
import com.sporekart.catalog.common.exception.DuplicateSlugException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Product create(CreateProductRequest request) {
        if (productRepositoryPort.existsBySku(request.sku())) {
            throw new DuplicateSkuException("SKU already exists");
        }
        if (productRepositoryPort.existsBySlug(request.slug())) {
            throw new DuplicateSlugException("Slug already exists");
        }
        Product product = Product.create(request.sku(), request.name(), request.slug(), request.description());
        return productRepositoryPort.save(product);
    }

    public List<Product> list() {
        return productRepositoryPort.findAll();
    }
}
