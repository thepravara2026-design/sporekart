package com.sporekart.customer.infrastructure.product;

import com.sporekart.customer.copilot.domain.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class ProductCatalogClient {

    private static final Logger log = LoggerFactory.getLogger(ProductCatalogClient.class);

    private final List<ProductItem> catalog = new CopyOnWriteArrayList<>();

    public ProductCatalogClient() {
        log.info("Initializing ProductCatalogClient with simulated catalog");
        catalog.add(new ProductItem("PROD-001", "Fresh Oyster Mushrooms", "Premium fresh oyster mushrooms, 200g pack. Perfect for stir-fries and soups.",
            "Fresh Mushrooms", 4.99, "USD", "/images/oyster-fresh.jpg", 150, true, 4.7,
            List.of("fresh", "oyster", "organic", "popular")));
        catalog.add(new ProductItem("PROD-002", "Fresh Shiitake Mushrooms", "High-quality fresh shiitake mushrooms, 150g pack. Rich, earthy flavor.",
            "Fresh Mushrooms", 6.99, "USD", "/images/shiitake-fresh.jpg", 120, true, 4.8,
            List.of("fresh", "shiitake", "premium", "popular")));
        catalog.add(new ProductItem("PROD-003", "Fresh Lion's Mane Mushrooms", "Fresh lion's mane mushrooms, 100g pack. Known for cognitive benefits.",
            "Fresh Mushrooms", 12.99, "USD", "/images/lionsmane-fresh.jpg", 45, true, 4.9,
            List.of("fresh", "lionsmane", "premium", "gourmet")));
        catalog.add(new ProductItem("PROD-004", "Fresh King Oyster Mushrooms", "Large king oyster mushrooms, 250g pack. Meaty texture.",
            "Fresh Mushrooms", 7.99, "USD", "/images/kingoyster-fresh.jpg", 80, true, 4.6,
            List.of("fresh", "king-oyster", "gourmet")));
        catalog.add(new ProductItem("PROD-005", "Fresh Enoki Mushrooms", "Delicate enoki mushrooms, 100g pack. Great for Asian cuisine.",
            "Fresh Mushrooms", 3.99, "USD", "/images/enoki-fresh.jpg", 200, true, 4.5,
            List.of("fresh", "enoki", "delicate")));
        catalog.add(new ProductItem("PROD-006", "Oyster Mushroom Growing Kit", "Complete grow kit for oyster mushrooms. Includes substrate and instructions.",
            "Growing Kits", 29.99, "USD", "/images/oyster-kit.jpg", 75, true, 4.9,
            List.of("kit", "oyster", "beginner", "indoor", "popular")));
        catalog.add(new ProductItem("PROD-007", "Shiitake Mushroom Log", "Pre-inoculated shiitake log. Produces multiple harvests over 3-5 years.",
            "Growing Kits", 39.99, "USD", "/images/shiitake-log.jpg", 30, true, 4.7,
            List.of("kit", "shiitake", "intermediate", "outdoor", "log")));
        catalog.add(new ProductItem("PROD-008", "Mushroom Garden Starter Kit", "All-in-one mushroom garden with 3 varieties. Great gift idea.",
            "Growing Kits", 49.99, "USD", "/images/garden-kit.jpg", 50, true, 4.8,
            List.of("kit", "starter", "beginner", "gift", "indoor")));
        catalog.add(new ProductItem("PROD-009", "Oyster Mushroom Spawn", "High-quality oyster mushroom spawn, 1kg. For experienced growers.",
            "Spawn", 15.99, "USD", "/images/oyster-spawn.jpg", 100, true, 4.6,
            List.of("spawn", "oyster", "intermediate", "bulk")));
        catalog.add(new ProductItem("PROD-010", "Shiitake Mushroom Spawn", "Premium shiitake spawn, 1kg bag. Suitable for log and sawdust cultivation.",
            "Spawn", 18.99, "USD", "/images/shiitake-spawn.jpg", 85, true, 4.7,
            List.of("spawn", "shiitake", "intermediate", "bulk")));
        catalog.add(new ProductItem("PROD-011", "Lion's Mane Mushroom Spawn", "Specialized lion's mane spawn, 500g. For gourmet growers.",
            "Spawn", 22.99, "USD", "/images/lionsmane-spawn.jpg", 40, true, 4.8,
            List.of("spawn", "lionsmane", "advanced", "gourmet")));
        catalog.add(new ProductItem("PROD-012", "Digital Humidity Monitor", "Accurate humidity and temperature monitor with probe for grow rooms.",
            "Equipment", 24.99, "USD", "/images/humidity-monitor.jpg", 200, true, 4.5,
            List.of("equipment", "humidity", "monitor", "essential")));
        catalog.add(new ProductItem("PROD-013", "Mushroom Grow Tent 60x60x120cm", "Compact grow tent with mylar reflective interior and ventilation ports.",
            "Equipment", 89.99, "USD", "/images/grow-tent.jpg", 25, true, 4.6,
            List.of("equipment", "tent", "indoor", "intermediate")));
        catalog.add(new ProductItem("PROD-014", "Professional Spray Bottle 500ml", "Fine mist spray bottle for maintaining humidity in grow areas.",
            "Equipment", 9.99, "USD", "/images/spray-bottle.jpg", 300, true, 4.4,
            List.of("equipment", "spray", "essential", "beginner")));
        catalog.add(new ProductItem("PROD-015", "Sterilized Substrate Mix 5kg", "Ready-to-use sterilized substrate mix for oyster and shiitake.",
            "Substrate", 19.99, "USD", "/images/substrate-mix.jpg", 60, true, 4.5,
            List.of("substrate", "sterilized", "oyster", "shiitake")));
        catalog.add(new ProductItem("PROD-016", "Mushroom Cultivation Book", "Comprehensive guide to growing mushrooms at home. 250 pages.",
            "Books", 24.99, "USD", "/images/cultivation-book.jpg", 90, true, 4.8,
            List.of("book", "education", "beginner", "reference")));
        catalog.add(new ProductItem("PROD-017", "Reishi Mushroom Capsules", "Organic reishi mushroom supplement, 60 capsules. Immune support.",
            "Supplements", 34.99, "USD", "/images/reishi-capsules.jpg", 110, true, 4.7,
            List.of("supplement", "reishi", "health", "organic")));
        catalog.add(new ProductItem("PROD-018", "Fresh Porcini Mushrooms", "Wild-harvested porcini mushrooms, 100g. Seasonal delicacy.",
            "Fresh Mushrooms", 19.99, "USD", "/images/porcini-fresh.jpg", 15, true, 4.9,
            List.of("fresh", "porcini", "gourmet", "seasonal", "premium")));
        log.info("ProductCatalogClient initialized with {} products", catalog.size());
    }

    public List<ProductItem> searchProducts(String query, Map<String, String> filters) {
        log.debug("ProductCatalogClient.searchProducts called with query='{}', filters={}", query, filters);
        return catalog.stream()
            .filter(p -> {
                if (query == null || query.isBlank()) return true;
                String q = query.toLowerCase();
                return p.name().toLowerCase().contains(q)
                    || p.description().toLowerCase().contains(q)
                    || p.category().toLowerCase().contains(q)
                    || p.tags().stream().anyMatch(t -> t.toLowerCase().contains(q));
            })
            .filter(p -> {
                if (filters == null) return true;
                String category = filters.get("category");
                if (category != null && !p.category().equalsIgnoreCase(category)) return false;
                String minPriceStr = filters.get("minPrice");
                if (minPriceStr != null) {
                    try {
                        double minPrice = Double.parseDouble(minPriceStr);
                        if (p.price() < minPrice) return false;
                    } catch (NumberFormatException e) {
                        log.warn("Invalid minPrice filter: {}", minPriceStr);
                    }
                }
                String maxPriceStr = filters.get("maxPrice");
                if (maxPriceStr != null) {
                    try {
                        double maxPrice = Double.parseDouble(maxPriceStr);
                        if (p.price() > maxPrice) return false;
                    } catch (NumberFormatException e) {
                        log.warn("Invalid maxPrice filter: {}", maxPriceStr);
                    }
                }
                String availableOnly = filters.get("availableOnly");
                if ("true".equalsIgnoreCase(availableOnly) && !p.isAvailable()) return false;
                return true;
            })
            .toList();
    }

    public Optional<ProductItem> getProduct(String id) {
        log.debug("ProductCatalogClient.getProduct called for id='{}'", id);
        return catalog.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    public List<ProductItem> getProductsByIds(List<String> ids) {
        log.debug("ProductCatalogClient.getProductsByIds called for {} ids", ids != null ? ids.size() : 0);
        if (ids == null || ids.isEmpty()) return List.of();
        return catalog.stream()
            .filter(p -> ids.contains(p.id()))
            .toList();
    }

    public List<ProductItem> getCategoryProducts(String category) {
        log.debug("ProductCatalogClient.getCategoryProducts called for category='{}'", category);
        if (category == null || category.isBlank()) return List.of();
        return catalog.stream()
            .filter(p -> p.category().equalsIgnoreCase(category))
            .toList();
    }

    public Map<String, Object> checkAvailability(String productId) {
        log.debug("ProductCatalogClient.checkAvailability called for productId='{}'", productId);
        var productOpt = getProduct(productId);
        if (productOpt.isEmpty()) {
            return Map.of("available", false, "reason", "Product not found", "stockCount", 0);
        }
        var product = productOpt.get();
        boolean inStock = product.stockLevel() > 0 && product.isAvailable();
        return Map.of(
            "available", inStock,
            "stockCount", product.stockLevel(),
            "estimatedRestockDays", inStock ? 0 : 14,
            "maxOrderQuantity", product.stockLevel()
        );
    }
}
