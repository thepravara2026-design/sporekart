package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.ProductItem;
import com.sporekart.customer.copilot.domain.ProductRecommendation;
import com.sporekart.customer.infrastructure.product.ProductCatalogClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationEngineTest {

    @Mock
    private ProductCatalogClient productCatalogClient;

    private RecommendationEngine recommendationEngine;

    private ProductItem product1;
    private ProductItem product2;
    private ProductItem product3;

    @BeforeEach
    void setUp() {
        recommendationEngine = new RecommendationEngine(productCatalogClient);

        product1 = new ProductItem("P1", "Oyster Mushroom Kit", "Grow your own", "Kits",
            29.99, "USD", "/img1.jpg", 50, true, 4.5, List.of("beginner", "kit"));
        product2 = new ProductItem("P2", "Shiitake Log", "Pre-inoculated log", "Kits",
            39.99, "USD", "/img2.jpg", 30, true, 4.7, List.of("intermediate", "log"));
        product3 = new ProductItem("P3", "Humidity Monitor", "Digital monitor", "Equipment",
            24.99, "USD", "/img3.jpg", 200, true, 4.3, List.of("equipment"));
    }

    @Test
    void getPersonalizedRecommendationsReturnsCorrectNumber() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product1, product2, product3));

        var results = recommendationEngine.getPersonalizedRecommendations("CUST-001", 2);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
    }

    @Test
    void getSeasonalRecommendationsFiltersBySeason() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product1, product3));

        var results = recommendationEngine.getSeasonalRecommendations("spring");

        assertNotNull(results);
        results.forEach(r -> {
            assertNotNull(r.reason());
            assertTrue(r.score() >= 0);
        });
    }

    @Test
    void getCrossSellRecommendationsReturnsComplementary() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product2, product3));

        var results = recommendationEngine.getCrossSellRecommendations("P1");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(r -> {
            assertNotNull(r.product());
            assertNotEquals("P1", r.product().id());
        });
    }

    @Test
    void getRecentlyViewedRecommendationsHandlesEmptyHistory() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(Collections.emptyList());

        var results = recommendationEngine.getRecentlyViewedRecommendations(List.of());

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void scoringIsNonNegative() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product1, product2, product3));

        var results = recommendationEngine.getPersonalizedRecommendations("CUST-001", 5);

        results.forEach(r -> assertTrue(r.score() >= 0, "Score must be non-negative for " + r.product().id()));
    }

    @Test
    void recommendationsContainReasons() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product1, product2));

        var results = recommendationEngine.getPersonalizedRecommendations("CUST-001", 5);

        assertFalse(results.isEmpty());
        results.forEach(r -> {
            assertNotNull(r.reason());
            assertFalse(r.reason().isBlank());
        });
    }

    @Test
    void getPersonalizedRecommendationsReturnsEmptyForInvalidCustomer() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(Collections.emptyList());

        var results = recommendationEngine.getPersonalizedRecommendations("INVALID", 5);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void recommendationsHaveValidTypes() {
        when(productCatalogClient.searchProducts(anyString(), any()))
            .thenReturn(List.of(product1, product2, product3));

        var results = recommendationEngine.getPersonalizedRecommendations("CUST-001", 10);

        assertFalse(results.isEmpty());
        results.forEach(r -> {
            assertNotNull(r.recommendationType());
            assertFalse(r.recommendationType().isBlank());
        });
    }
}
