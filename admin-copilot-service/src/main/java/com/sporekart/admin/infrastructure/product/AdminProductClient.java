package com.sporekart.admin.infrastructure.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminProductClient {

    private static final Logger log = LoggerFactory.getLogger(AdminProductClient.class);
    private static final Random RAND = new Random(42);

    public Map<String, Object> getProductCatalog() {
        log.info("Fetching product catalog");
        Map<String, Object> catalog = new LinkedHashMap<>();
        catalog.put("totalProducts", 18);
        catalog.put("activeProducts", 16);
        catalog.put("totalCategories", 5);
        catalog.put("lastUpdated", LocalDate.now().toString());

        List<Map<String, Object>> categories = new ArrayList<>();
        String[][] catData = {
                {"Mushroom Kits", "4", "Fastest growing category", "35"},
                {"Spawn & Substrates", "3", "High repeat purchase", "20"},
                {"Equipment", "4", "Seasonal demand", "18"},
                {"Processed Products", "3", "Premium margin", "15"},
                {"Growing Supplies", "4", "Steady demand", "12"}
        };

        for (String[] cat : catData) {
            Map<String, Object> c = new LinkedHashMap<>();
            c.put("name", cat[0]);
            c.put("productCount", Integer.parseInt(cat[1]));
            c.put("description", cat[2]);
            c.put("revenueShare", Integer.parseInt(cat[3]));
            categories.add(c);
        }
        catalog.put("categories", categories);

        List<Map<String, Object>> featured = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Map<String, Object> p = new LinkedHashMap<>();
            p.put("id", "PROD-" + (2000 + i));
            p.put("name", new String[]{
                    "Mushroom Grow Kit - Oyster", "Organic Compost 5kg", "Spawn Bag - Oyster 2kg",
                    "Mushroom Powder - Shiitake 250g", "Misting System Pro", "Mushroom Starter Kit - Deluxe"
            }[i - 1]);
            p.put("price", 299 + RAND.nextInt(1500));
            p.put("rating", Math.round((3.5 + RAND.nextDouble() * 1.5) * 10.0) / 10.0);
            p.put("reviewCount", 5 + RAND.nextInt(95));
            p.put("inStock", RAND.nextDouble() > 0.15);
            featured.add(p);
        }
        catalog.put("featuredProducts", featured);

        return catalog;
    }

    public List<Map<String, Object>> getProductPerformance() {
        log.info("Fetching product performance data");
        List<Map<String, Object>> performance = new ArrayList<>();
        String[] products = {
                "Mushroom Grow Kit - Oyster", "Mushroom Grow Kit - Shiitake", "Organic Compost 5kg",
                "Spawn Bag - Oyster 2kg", "Spawn Bag - Button 2kg", "Misting System Pro",
                "Humidity Controller", "pH Testing Kit", "Sterilized Rye Grain 5kg", "Coco Coir Block 650g",
                "Mushroom Harvesting Knife", "Drying Rack - Stainless", "Mushroom Powder - Oyster 250g",
                "Mushroom Powder - Shiitake 250g", "Pickled Mushroom Jar 500g", "Mushroom Starter Kit - Deluxe",
                "Lion's Mane Grow Kit", "Organic Vermiculite 2L"
        };
        String[] categories = {
                "Mushroom Kits", "Mushroom Kits", "Growing Supplies", "Spawn", "Spawn",
                "Equipment", "Equipment", "Equipment", "Supplies", "Growing Supplies",
                "Equipment", "Equipment", "Processed", "Processed", "Processed",
                "Mushroom Kits", "Mushroom Kits", "Growing Supplies"
        };

        for (int i = 0; i < products.length; i++) {
            Map<String, Object> p = new LinkedHashMap<>();
            p.put("productId", "PROD-" + (2000 + i));
            p.put("name", products[i]);
            p.put("category", categories[i]);
            p.put("unitsSold", 10 + RAND.nextInt(500));
            p.put("revenue", 5000 + RAND.nextDouble() * 200000);
            p.put("growthRate", Math.round((-10 + RAND.nextDouble() * 30) * 10.0) / 10.0);
            p.put("stockLevel", 10 + RAND.nextInt(200));
            p.put("conversionRate", Math.round((1.5 + RAND.nextDouble() * 5) * 10.0) / 10.0);
            performance.add(p);
        }

        return performance.stream()
                .sorted((a, b) -> Double.compare((double) b.get("revenue"), (double) a.get("revenue")))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getCategoryMetrics() {
        log.info("Fetching category metrics");
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalCategories", 5);
        metrics.put("topCategory", "Mushroom Kits");
        metrics.put("topCategoryRevenue", 35.0);
        metrics.put("growingCategory", "Equipment");
        metrics.put("growingCategoryGrowth", 22.5);

        List<Map<String, Object>> categoryDetails = new ArrayList<>();
        String[][] cats = {
                {"Mushroom Kits", "35%", "18%", "4.5"},
                {"Spawn & Substrates", "20%", "12%", "4.2"},
                {"Equipment", "18%", "22%", "4.0"},
                {"Processed Products", "15%", "8%", "4.3"},
                {"Growing Supplies", "12%", "10%", "4.1"}
        };
        for (String[] c : cats) {
            Map<String, Object> cat = new LinkedHashMap<>();
            cat.put("name", c[0]);
            cat.put("revenueShare", c[1]);
            cat.put("growthRate", c[2]);
            cat.put("averageRating", Double.parseDouble(c[3]));
            categoryDetails.add(cat);
        }
        metrics.put("categoryDetails", categoryDetails);

        return metrics;
    }
}
