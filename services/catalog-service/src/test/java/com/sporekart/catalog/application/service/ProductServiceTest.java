package com.sporekart.catalog.application.service;

import com.sporekart.catalog.application.dto.CreateProductRequest;
import com.sporekart.catalog.domain.model.Product;
import com.sporekart.catalog.domain.repository.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    @Test
    void createProductPersistsValidProduct() {
        ProductRepositoryPort repositoryPort = Mockito.mock(ProductRepositoryPort.class);
        when(repositoryPort.existsBySku(any())).thenReturn(false);
        when(repositoryPort.existsBySlug(any())).thenReturn(false);
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ProductService service = new ProductService(repositoryPort);

        Product product = service.create(new CreateProductRequest("SKU1", "Mushroom", "mushroom", "Fresh"));

        assertNotNull(product);
    }
}
