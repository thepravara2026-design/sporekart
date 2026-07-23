package com.sporekart.admin.engine;

import com.sporekart.admin.domain.*;
import com.sporekart.admin.dto.AnalyticsQuery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class AnalyticsEngine {

    public AdminDashboard getDashboard() {
        return new AdminDashboard(
            BigDecimal.valueOf(1250000),
            450,
            8920,
            3400,
            1250,
            23,
            18,
            BigDecimal.valueOf(48500),
            BigDecimal.valueOf(12.5),
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now()
        );
    }

    public SalesSummary getSalesSummary(AnalyticsQuery query) {
        Map<String, BigDecimal> revenueByCategory = new LinkedHashMap<>();
        revenueByCategory.put("Produce", BigDecimal.valueOf(450000));
        revenueByCategory.put("Dairy", BigDecimal.valueOf(320000));
        revenueByCategory.put("Bakery", BigDecimal.valueOf(210000));
        revenueByCategory.put("Beverages", BigDecimal.valueOf(180000));
        revenueByCategory.put("Snacks", BigDecimal.valueOf(90000));

        return new SalesSummary(
            query.period(),
            BigDecimal.valueOf(1250000),
            450,
            BigDecimal.valueOf(2778),
            BigDecimal.valueOf(12.5),
            List.of("Fresh Vegetables Pack", "Organic Milk", "Sourdough Bread", "Cold Coffee", "Mixed Nuts"),
            revenueByCategory,
            450
        );
    }

    public CustomerInsight getCustomerInsights() {
        return new CustomerInsight(
            8920,
            1240,
            7680,
            BigDecimal.valueOf(8.5),
            BigDecimal.valueOf(91.5),
            BigDecimal.valueOf(45000),
            List.of("FreshMart Delhi", "Organic Stores Mumbai", "GreenFarms Bangalore", "DailyFresh Pune", "NatureBasket Chennai"),
            520
        );
    }

    public InventoryInsight getInventoryInsights() {
        List<Map<String, Object>> lowStockItems = new ArrayList<>();
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("product", "Organic Tomatoes");
        item.put("currentStock", 12);
        item.put("minimumRequired", 50);
        item.put("reorderQuantity", 200);
        lowStockItems.add(item);

        item = new LinkedHashMap<>();
        item.put("product", "Fresh Milk");
        item.put("currentStock", 8);
        item.put("minimumRequired", 30);
        item.put("reorderQuantity", 100);
        lowStockItems.add(item);

        item = new LinkedHashMap<>();
        item.put("product", "Wheat Flour");
        item.put("currentStock", 5);
        item.put("minimumRequired", 40);
        item.put("reorderQuantity", 150);
        lowStockItems.add(item);

        return new InventoryInsight(
            3400,
            52000,
            lowStockItems,
            List.of(),
            List.of(
                Map.of("product", "Milk", "sold", 1200),
                Map.of("product", "Bread", "sold", 980),
                Map.of("product", "Eggs", "sold", 850)
            ),
            List.of(
                Map.of("product", "Organic Spices", "sold", 45),
                Map.of("product", "Imported Cheese", "sold", 32)
            ),
            BigDecimal.valueOf(8500000),
            BigDecimal.valueOf(6.2)
        );
    }

    public Map<String, Object> getRevenueAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        analytics.put("totalRevenue", 1250000);
        analytics.put("monthlyTrend", List.of(380000, 410000, 430000, 460000, 480000, 500000));
        analytics.put("growthRate", 12.5);
        analytics.put("averageMonthlyRevenue", 416667);
        analytics.put("topCategory", "Produce");
        analytics.put("revenueByCategory", Map.of(
            "Produce", 450000,
            "Dairy", 320000,
            "Bakery", 210000,
            "Beverages", 180000,
            "Snacks", 90000
        ));
        return analytics;
    }

    public Map<String, Object> getPlatformHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("uptime", "99.97%");
        health.put("status", "healthy");
        health.put("responseTime", 245);
        health.put("activeUsers", 1250);
        health.put("errorRate", 0.03);
        health.put("lastIncident", LocalDate.now().minusDays(45).toString());
        health.put("services", Map.of(
            "api", "operational",
            "database", "operational",
            "cache", "operational",
            "queue", "operational"
        ));
        return health;
    }
}
