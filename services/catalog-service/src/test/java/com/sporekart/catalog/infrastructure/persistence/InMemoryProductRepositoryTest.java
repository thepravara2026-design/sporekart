package com.sporekart.catalog.infrastructure.persistence;

import com.sporekart.catalog.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryProductRepositoryTest {
    @Test
    void saveAndFindByIdRoundTrip() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        Product product = Product.create("SKU-1", "Mushroom", "mushroom", "Fresh");

        Product saved = repository.save(product);

        assertEquals(saved.getId(), repository.findById(saved.getId()).orElseThrow().getId());
    }

    @Test
    void detectsExistingSkuAndSlug() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        Product product = Product.create("SKU-2", "Truffle", "truffle", "Aroma");
        repository.save(product);

        assertTrue(repository.existsBySku("SKU-2"));
        assertTrue(repository.existsBySlug("truffle"));
        assertFalse(repository.existsBySku("SKU-3"));
        assertFalse(repository.existsBySlug("unknown"));
    }
}
