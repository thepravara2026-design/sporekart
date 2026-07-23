package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.DashboardWidget;
import com.sporekart.bi.copilot.dto.DashboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DashboardEngineTest {

    private DashboardEngine engine;

    @BeforeEach
    void setUp() {
        engine = new DashboardEngine();
    }

    @Test
    void getDefaultDashboard_shouldReturnDashboard() {
        DashboardResponse dashboard = engine.getDefaultDashboard();
        assertNotNull(dashboard);
        assertNotNull(dashboard.dashboardId());
        assertTrue(dashboard.widgets().size() >= 4);
    }

    @Test
    void getDefaultDashboard_shouldHaveRevenueWidget() {
        DashboardResponse dashboard = engine.getDefaultDashboard();
        assertTrue(dashboard.widgets().stream().anyMatch(w -> w.metric().contains("revenue")));
    }

    @Test
    void getExecutiveDashboard_shouldReturnDashboardWithInsights() {
        DashboardResponse dashboard = engine.getExecutiveDashboard();
        assertNotNull(dashboard);
        assertFalse(dashboard.insights().isEmpty());
    }

    @Test
    void getExecutiveDashboard_shouldHaveSummaryData() {
        DashboardResponse dashboard = engine.getExecutiveDashboard();
        assertNotNull(dashboard.summaryData());
        assertTrue(dashboard.summaryData().containsKey("totalRevenue"));
    }

    @Test
    void getOperationsDashboard_shouldReturnDashboard() {
        DashboardResponse dashboard = engine.getOperationsDashboard();
        assertNotNull(dashboard);
        assertFalse(dashboard.widgets().isEmpty());
    }

    @Test
    void createWidget_shouldReturnCreatedWidget() {
        DashboardWidget widget = engine.createWidget("Revenue Trend", "line_chart", "revenue",
                Map.of("period", "2025-06"), 2, 1, 0, 0, "1h", true);
        assertNotNull(widget);
        assertNotNull(widget.widgetId());
        assertEquals("Revenue Trend", widget.title());
    }

    @Test
    void getWidgetData_shouldReturnDataForExistingWidget() {
        DashboardWidget widget = engine.createWidget("Test", "bar", "revenue",
                Map.of(), 1, 1, 0, 0, "5m", true);
        DashboardResponse data = engine.getWidgetData(widget.widgetId());
        assertNotNull(data);
    }

    @Test
    void refreshDashboard_shouldReturnRefreshedDashboard() {
        DashboardResponse refreshed = engine.refreshDashboard("default");
        assertNotNull(refreshed);
        assertTrue(refreshed.widgets().size() >= 4);
    }

    @Test
    void refreshDashboard_shouldReturnDefaultForUnknownId() {
        DashboardResponse refreshed = engine.refreshDashboard("unknown");
        assertNotNull(refreshed);
    }

    @Test
    void getDefaultDashboard_shouldHaveWidgetsWithConfig() {
        DashboardResponse dashboard = engine.getDefaultDashboard();
        assertTrue(dashboard.widgets().stream().allMatch(w -> w.configuration() != null));
    }

    @Test
    void getExecutiveDashboard_shouldHaveHighPriorityInsights() {
        DashboardResponse dashboard = engine.getExecutiveDashboard();
        long criticalOrHigh = dashboard.insights().stream()
                .filter(i -> "critical".equals(i.severity()) || "high".equals(i.severity()))
                .count();
        assertTrue(criticalOrHigh > 0);
    }

    @Test
    void createWidget_shouldSetEnabledToTrue() {
        DashboardWidget widget = engine.createWidget("Test", "metric", "orders",
                Map.of(), 1, 1, 0, 0, null, true);
        assertTrue(widget.enabled());
    }
}
