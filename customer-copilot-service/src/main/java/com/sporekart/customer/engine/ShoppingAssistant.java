package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.ProductItem;
import com.sporekart.customer.copilot.domain.ProductRecommendation;
import com.sporekart.customer.infrastructure.product.ProductCatalogClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShoppingAssistant {

    private final ProductCatalogClient productCatalogClient;
    private final Random random = new Random();

    public ShoppingAssistant(ProductCatalogClient productCatalogClient) {
        this.productCatalogClient = productCatalogClient;
    }

    public List<ProductItem> searchProducts(String keyword) {
        return productCatalogClient.searchProducts(keyword, null);
    }

    public List<ProductItem> searchProducts(String keyword, String category) {
        var filters = new HashMap<String, String>();
        if (category != null && !category.isBlank()) {
            filters.put("category", category);
        }
        return productCatalogClient.searchProducts(keyword, filters);
    }

    public List<ProductItem> searchProducts(String keyword, String category, Double minPrice, Double maxPrice) {
        var filters = new HashMap<String, String>();
        if (category != null && !category.isBlank()) {
            filters.put("category", category);
        }
        if (minPrice != null) {
            filters.put("minPrice", String.valueOf(minPrice));
        }
        if (maxPrice != null) {
            filters.put("maxPrice", String.valueOf(maxPrice));
        }
        return productCatalogClient.searchProducts(keyword, filters);
    }

    public List<ProductItem> compareProducts(String productId1, String productId2) {
        var p1 = productCatalogClient.getProduct(productId1);
        var p2 = productCatalogClient.getProduct(productId2);
        var result = new ArrayList<ProductItem>();
        p1.ifPresent(result::add);
        p2.ifPresent(result::add);
        return result;
    }

    public Optional<ProductItem> getProductDetails(String productId) {
        return productCatalogClient.getProduct(productId);
    }
}
