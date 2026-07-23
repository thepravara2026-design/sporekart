package com.sporekart.admin.engine;

import com.sporekart.admin.domain.*;
import com.sporekart.admin.dto.AnalyticsQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEngineTest {

    private AnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AnalyticsEngine();
    }

    @Test
    void getDashboardShouldReturnAllMetricsPopulated() {
        AdminDashboard dashboard = engine.getDashboard();

        assertNotNull(dashboard);
        assertNotNull(dashboard.totalRevenue());
        assertTrue(dashboard.totalRevenue().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(dashboard.totalOrders() > 0);
        assertTrue(dashboard.totalCustomers() > 0);
        assertTrue(dashboard.totalProducts() > 0);
        assertTrue(dashboard.activeUsers() > 0);
        assertTrue(dashboard.pendingOrders() >= 0);
        assertTrue(dashboard.lowStockItems() >= 0);
        assertNotNull(dashboard.todaySales());
        assertNotNull(dashboard.growthRate());
        assertNotNull(dashboard.periodStart());
        assertNotNull(dashboard.periodEnd());
    }

    @Test
    void getSalesSummaryForMonthlyPeriodShouldReturnData() {
        AnalyticsQuery query = new AnalyticsQuery("revenue", "MONTHLY", LocalDate.now().minusMonths(1), LocalDate.now(), Map.of());

        SalesSummary summary = engine.getSalesSummary(query);

        assertNotNull(summary);
        assertNotNull(summary.period());
        assertNotNull(summary.totalRevenue());
        assertTrue(summary.totalOrders() > 0);
        assertNotNull(summary.averageOrderValue());
        assertNotNull(summary.growthRate());
        assertNotNull(summary.topProducts());
        assertFalse(summary.topProducts().isEmpty());
        assertNotNull(summary.revenueByCategory());
        assertFalse(summary.revenueByCategory().isEmpty());
    }

    @Test
    void getCustomerInsightsShouldCalculateMetricsCorrectly() {
        CustomerInsight insight = engine.getCustomerInsights();

        assertNotNull(insight);
        assertTrue(insight.totalCustomers() > 0);
        assertTrue(insight.newCustomers() > 0);
        assertTrue(insight.returningCustomers() > 0);
        assertNotNull(insight.churnRate());
        assertTrue(insight.churnRate().compareTo(BigDecimal.ZERO) >= 0);
        assertNotNull(insight.retentionRate());
        assertTrue(insight.retentionRate().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(insight.retentionRate().compareTo(new BigDecimal("100")) <= 0);
        assertNotNull(insight.averageLifetimeValue());
        assertNotNull(insight.topCustomers());
        assertFalse(insight.topCustomers().isEmpty());
        assertTrue(insight.inactiveCustomers() >= 0);
    }

    @Test
    void getInventoryInsightsShouldIdentifyLowStock() {
        InventoryInsight insight = engine.getInventoryInsights();

        assertNotNull(insight);
        assertTrue(insight.totalProducts() > 0);
        assertTrue(insight.totalStock() > 0);
        assertNotNull(insight.lowStockItems());
        assertFalse(insight.lowStockItems().isEmpty());
        assertNotNull(insight.outOfStockItems());
        assertNotNull(insight.fastMoving());
        assertNotNull(insight.slowMoving());
        assertNotNull(insight.inventoryValue());
        assertNotNull(insight.turnoverRate());
    }

    @Test
    void getRevenueAnalyticsShouldShowGrowthTrends() {
        Map<String, Object> revenueAnalytics = engine.getRevenueAnalytics();

        assertNotNull(revenueAnalytics);
        assertTrue(revenueAnalytics.containsKey("totalRevenue"));
        assertTrue(revenueAnalytics.containsKey("monthlyTrend"));
        assertTrue(revenueAnalytics.containsKey("growthRate"));
        assertTrue(revenueAnalytics.containsKey("averageMonthlyRevenue"));
        assertTrue(revenueAnalytics.containsKey("topCategory"));
    }

    @Test
    void getPlatformHealthShouldReturnUptime() {
        Map<String, Object> health = engine.getPlatformHealth();

        assertNotNull(health);
        assertTrue(health.containsKey("uptime"));
        assertTrue(health.containsKey("status"));
        assertTrue(health.containsKey("responseTime"));
        assertTrue(health.containsKey("activeUsers"));
        assertTrue(health.containsKey("errorRate"));
    }
}
