package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.ProductItem;
import com.sporekart.customer.infrastructure.product.ProductCatalogClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingAssistantTest {

    @Mock
    private ProductCatalogClient productCatalogClient;

    private ShoppingAssistant shoppingAssistant;

    private ProductItem product1;
    private ProductItem product2;

    @BeforeEach
    void setUp() {
        shoppingAssistant = new ShoppingAssistant(productCatalogClient);

        product1 = new ProductItem("P1", "Oyster Mushroom Kit", "Grow your own oyster mushrooms",
            "Kits", 29.99, "USD", "/img1.jpg", 50, true, 4.5, List.of("oyster", "kit", "beginner"));
        product2 = new ProductItem("P2", "Shiitake Log", "Pre-inoculated shiitake log",
            "Kits", 39.99, "USD", "/img2.jpg", 30, true, 4.7, List.of("shiitake", "log", "intermediate"));
    }

    @Test
    void searchProductsByKeywordReturnsMatchingResults() {
        when(productCatalogClient.searchProducts(eq("oyster"), any()))
            .thenReturn(List.of(product1));

        var results = shoppingAssistant.searchProducts("oyster");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Oyster Mushroom Kit", results.get(0).name());
    }

    @Test
    void searchProductsWithCategoryFilterFiltersCorrectly() {
        when(productCatalogClient.searchProducts(any(), any()))
            .thenReturn(List.of(product1, product2));

        var results = shoppingAssistant.searchProducts("mushroom", "Kits");

        assertNotNull(results);
        assertEquals(2, results.size());
        results.forEach(p -> assertEquals("Kits", p.category()));
    }

    @Test
    void searchProductsWithPriceRangeReturnsFilteredResults() {
        when(productCatalogClient.searchProducts(any(), any()))
            .thenReturn(List.of(product1));

        var results = shoppingAssistant.searchProducts(null, null, 20.0, 30.0);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(p -> assertTrue(p.price() >= 20.0 && p.price() <= 30.0));
    }

    @Test
    void searchProductsWithEmptyResultsReturnsEmptyList() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of());

        var results = shoppingAssistant.searchProducts("zzzznonexistent");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void compareProductsReturnsBothProducts() {
        when(productCatalogClient.getProduct("P1")).thenReturn(Optional.of(product1));
        when(productCatalogClient.getProduct("P2")).thenReturn(Optional.of(product2));

        var results = shoppingAssistant.compareProducts("P1", "P2");

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void compareProductsWithMissingProductReturnsOnlyExisting() {
        when(productCatalogClient.getProduct("P1")).thenReturn(Optional.of(product1));
        when(productCatalogClient.getProduct("INVALID")).thenReturn(Optional.empty());

        var results = shoppingAssistant.compareProducts("P1", "INVALID");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("P1", results.get(0).id());
    }

    @Test
    void getProductDetailsForExistingProductReturnsProduct() {
        when(productCatalogClient.getProduct("P1")).thenReturn(Optional.of(product1));

        var result = shoppingAssistant.getProductDetails("P1");

        assertTrue(result.isPresent());
        assertEquals("P1", result.get().id());
        assertEquals("Oyster Mushroom Kit", result.get().name());
    }

    @Test
    void getProductDetailsForNonExistingProductReturnsEmpty() {
        when(productCatalogClient.getProduct("INVALID")).thenReturn(Optional.empty());

        var result = shoppingAssistant.getProductDetails("INVALID");

        assertTrue(result.isEmpty());
    }
}
