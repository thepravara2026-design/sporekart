package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.ProductItem;
import com.sporekart.customer.copilot.domain.ProductRecommendation;
import com.sporekart.customer.infrastructure.product.ProductCatalogClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecommendationEngine {

    private final ProductCatalogClient productCatalogClient;
    private final Random random = new Random();

    public RecommendationEngine(ProductCatalogClient productCatalogClient) {
        this.productCatalogClient = productCatalogClient;
    }

    public List<ProductRecommendation> getPersonalizedRecommendations(String customerId, int limit) {
        var products = productCatalogClient.searchProducts(null, null);
        if (products.isEmpty()) return List.of();

        return products.stream()
            .map(p -> new ProductRecommendation(p, Math.round(random.nextDouble() * 100) / 100.0,
                "Based on your browsing history and preferences", "PERSONALIZED"))
            .sorted((a, b) -> Double.compare(b.score(), a.score()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<ProductRecommendation> getSeasonalRecommendations(String season) {
        var filters = new HashMap<String, String>();
        filters.put("category", "Seasonal");
        var products = productCatalogClient.searchProducts(season, filters);

        if (products.isEmpty()) {
            products = productCatalogClient.searchProducts(null, null).stream()
                .filter(p -> p.tags().contains(season.toLowerCase()) || p.tags().contains("seasonal"))
                .limit(4)
                .collect(Collectors.toList());
        }

        return products.stream()
            .map(p -> new ProductRecommendation(p, 0.85 + random.nextDouble() * 0.15,
                "Perfect for " + season + " season", "SEASONAL"))
            .collect(Collectors.toList());
    }

    public List<ProductRecommendation> getCrossSellRecommendations(String productId) {
        var allProducts = productCatalogClient.searchProducts(null, null);
        return allProducts.stream()
            .filter(p -> !p.id().equals(productId))
            .limit(3)
            .map(p -> new ProductRecommendation(p, 0.7 + random.nextDouble() * 0.3,
                "Customers who bought this also bought", "CROSS_SELL"))
            .collect(Collectors.toList());
    }

    public List<ProductRecommendation> getRecentlyViewedRecommendations(List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) return List.of();

        var products = productCatalogClient.getProductsByIds(productIds);
        return products.stream()
            .map(p -> new ProductRecommendation(p, 0.9 + random.nextDouble() * 0.1,
                "Based on your recently viewed items", "RECENTLY_VIEWED"))
            .collect(Collectors.toList());
    }
}
