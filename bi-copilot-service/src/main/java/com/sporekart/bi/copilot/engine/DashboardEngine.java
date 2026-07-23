package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.DashboardWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DashboardEngine {

    private static final Logger log = LoggerFactory.getLogger(DashboardEngine.class);

    private final Map<String, DashboardConfig> dashboards;
    private final Map<String, DashboardWidget> widgets;

    public record DashboardConfig(
            String dashboardId,
            String name,
            String description,
            String type,
            List<String> widgetIds,
            Map<String, Object> layout,
            OffsetDateTime lastRefreshedAt
    ) {}

    public record DashboardResponse(
            String dashboardId,
            String name,
            String description,
            String type,
            List<DashboardWidget> widgets,
            Map<String, Object> layout,
            OffsetDateTime lastRefreshedAt,
            OffsetDateTime generatedAt
    ) {}

    public DashboardEngine() {
        this.dashboards = new ConcurrentHashMap<>();
        this.widgets = new ConcurrentHashMap<>();
        seedDashboards();
        log.info("Initialized DashboardEngine with {} dashboards and {} widgets",
                dashboards.size(), widgets.size());
    }

    private void seedDashboards() {
        List<DashboardWidget> execWidgets = List.of(
                new DashboardWidget("W_EXEC_001", "Total Revenue", "kpi_card", "revenue", "total_revenue",
                        Map.of("prefix", "Rs.", "format", "compact", "threshold", Map.of("good", 2000000, "warning", 1500000)),
                        3, 2, 0, 0, "5m", true),
                new DashboardWidget("W_EXEC_002", "Active Customers", "kpi_card", "customer", "active_customers",
                        Map.of("format", "number", "threshold", Map.of("good", 2500, "warning", 2000)),
                        3, 2, 3, 0, "5m", true),
                new DashboardWidget("W_EXEC_003", "Revenue Trend", "line_chart", "revenue", "monthly_revenue",
                        Map.of("period", "12m", "showTrend", true, "showForecast", true),
                        6, 4, 0, 2, "15m", true),
                new DashboardWidget("W_EXEC_004", "Training Completion Rate", "gauge", "training", "completion_rate",
                        Map.of("min", 0, "max", 100, "thresholds", Map.of("good", 80, "warning", 60, "danger", 40)),
                        3, 2, 6, 2, "10m", true),
                new DashboardWidget("W_EXEC_005", "Yield by Species", "bar_chart", "cultivation", "yield_by_species",
                        Map.of("chartType", "stacked", "showLegend", true, "topN", 5),
                        4, 3, 0, 6, "30m", true),
                new DashboardWidget("W_EXEC_006", "Customer Satisfaction", "kpi_card", "customer", "satisfaction_score",
                        Map.of("format", "decimal", "threshold", Map.of("good", 4.0, "warning", 3.0, "danger", 2.0)),
                        2, 2, 6, 4, "1h", true)
        );
        execWidgets.forEach(w -> widgets.put(w.widgetId(), w));
        dashboards.put("DASH_EXEC", new DashboardConfig("DASH_EXEC", "Executive Dashboard",
                "High-level business performance overview for leadership", "executive",
                execWidgets.stream().map(DashboardWidget::widgetId).collect(Collectors.toList()),
                Map.of("columns", 9, "rowHeight", 100, "margin", 10), OffsetDateTime.now()));

        List<DashboardWidget> opsWidgets = List.of(
                new DashboardWidget("W_OPS_001", "Active Batches", "kpi_card", "training", "active_batches",
                        Map.of("format", "number"), 2, 2, 0, 0, "5m", true),
                new DashboardWidget("W_OPS_002", "Contamination Rate", "kpi_card", "cultivation", "contamination_rate",
                        Map.of("format", "percent", "threshold", Map.of("good", 3.0, "warning", 5.0, "danger", 8.0)),
                        2, 2, 2, 0, "10m", true),
                new DashboardWidget("W_OPS_003", "Top Products", "table", "revenue", "top_products",
                        Map.of("columns", List.of("Product", "Revenue", "Growth"), "pageSize", 5),
                        4, 4, 4, 0, "30m", true),
                new DashboardWidget("W_OPS_004", "Pending Orders", "kpi_card", "revenue", "pending_orders",
                        Map.of("format", "number", "threshold", Map.of("warning", 50, "danger", 100)),
                        2, 2, 0, 2, "5m", true),
                new DashboardWidget("W_OPS_005", "Cycle Time Trend", "line_chart", "cultivation", "cycle_time",
                        Map.of("period", "6m", "showTarget", true, "target", 42),
                        4, 3, 2, 2, "1h", true),
                new DashboardWidget("W_OPS_006", "Inventory Levels", "bar_chart", "inventory", "stock_levels",
                        Map.of("chartType", "horizontal", "threshold", Map.of("low", 20)),
                        3, 3, 6, 2, "15m", true),
                new DashboardWidget("W_OPS_007", "Recent Alerts", "list", "system", "alerts",
                        Map.of("maxItems", 10, "severity", "all", "showTimestamp", true),
                        4, 3, 0, 5, "1m", true),
                new DashboardWidget("W_OPS_008", "Batch Yield Distribution", "scatter_plot", "cultivation", "yield_distribution",
                        Map.of("xAxis", "batch_size", "yAxis", "yield_kg", "showCorrelation", true),
                        4, 3, 4, 5, "30m", true)
        );
        opsWidgets.forEach(w -> widgets.put(w.widgetId(), w));
        dashboards.put("DASH_OPS", new DashboardConfig("DASH_OPS", "Operations Dashboard",
                "Day-to-day operational metrics for production and fulfillment teams", "operations",
                opsWidgets.stream().map(DashboardWidget::widgetId).collect(Collectors.toList()),
                Map.of("columns", 10, "rowHeight", 100, "margin", 10), OffsetDateTime.now()));

        List<DashboardWidget> mktgWidgets = List.of(
                new DashboardWidget("W_MKTG_001", "New Customers", "kpi_card", "customer", "new_customers",
                        Map.of("format", "number", "comparison", "previous_period"), 2, 2, 0, 0, "5m", true),
                new DashboardWidget("W_MKTG_002", "Customer Growth Trend", "line_chart", "customer", "customer_growth",
                        Map.of("period", "12m", "showSegments", true, "segments", List.of("New", "Returning", "Churned")),
                        5, 3, 2, 0, "15m", true),
                new DashboardWidget("W_MKTG_003", "Revenue by Channel", "pie_chart", "revenue", "revenue_by_channel",
                        Map.of("showPercentage", true, "showLegend", true),
                        3, 3, 0, 2, "30m", true),
                new DashboardWidget("W_MKTG_004", "Customer Segments", "donut_chart", "customer", "segment_distribution",
                        Map.of("showPercentage", true, "segmentColors", Map.of("High-Value", "#2E7D32", "New Grower", "#1565C0",
                                "Hobbyist", "#6A1B9A", "Commercial", "#E65100", "Distributor", "#00838F", "At-Risk", "#C62828")),
                        3, 3, 3, 2, "1h", true),
                new DashboardWidget("W_MKTG_005", "Marketing Campaign ROI", "table", "marketing", "campaign_roi",
                        Map.of("columns", List.of("Campaign", "Spend", "Revenue", "ROI", "Status"), "pageSize", 5),
                        5, 3, 0, 5, "1h", true)
        );
        mktgWidgets.forEach(w -> widgets.put(w.widgetId(), w));
        dashboards.put("DASH_MKTG", new DashboardConfig("DASH_MKTG", "Marketing Dashboard",
                "Marketing performance and customer acquisition metrics", "marketing",
                mktgWidgets.stream().map(DashboardWidget::widgetId).collect(Collectors.toList()),
                Map.of("columns", 8, "rowHeight", 100, "margin", 10), OffsetDateTime.now()));
    }

    public DashboardResponse getDashboard(String dashboardId) {
        log.debug("Fetching dashboard: {}", dashboardId);
        DashboardConfig config = dashboards.get(dashboardId);
        if (config == null) {
            throw new IllegalArgumentException("Dashboard not found: " + dashboardId);
        }
        return toResponse(config);
    }

    public DashboardResponse getDefaultDashboard() {
        log.debug("Returning default dashboard");
        return getDashboard("DASH_EXEC");
    }

    public DashboardWidget createWidget(DashboardWidget widget) {
        log.debug("Creating widget: {}", widget.title());
        DashboardWidget saved = new DashboardWidget(
                widget.widgetId() != null && !widget.widgetId().isBlank() ? widget.widgetId() : UUID.randomUUID().toString(),
                widget.title(), widget.widgetType(), widget.dataSource(), widget.metric(),
                widget.configuration(), widget.width(), widget.height(),
                widget.positionX(), widget.positionY(), widget.refreshInterval(), widget.enabled()
        );
        widgets.put(saved.widgetId(), saved);
        return saved;
    }

    public DashboardWidget updateWidget(String widgetId, DashboardWidget updates) {
        log.debug("Updating widget: {}", widgetId);
        DashboardWidget existing = widgets.get(widgetId);
        if (existing == null) {
            throw new IllegalArgumentException("Widget not found: " + widgetId);
        }
        DashboardWidget updated = new DashboardWidget(
                widgetId,
                updates.title() != null ? updates.title() : existing.title(),
                updates.widgetType() != null ? updates.widgetType() : existing.widgetType(),
                updates.dataSource() != null ? updates.dataSource() : existing.dataSource(),
                updates.metric() != null ? updates.metric() : existing.metric(),
                updates.configuration() != null ? updates.configuration() : existing.configuration(),
                updates.width() > 0 ? updates.width() : existing.width(),
                updates.height() > 0 ? updates.height() : existing.height(),
                updates.positionX() >= 0 ? updates.positionX() : existing.positionX(),
                updates.positionY() >= 0 ? updates.positionY() : existing.positionY(),
                updates.refreshInterval() != null ? updates.refreshInterval() : existing.refreshInterval(),
                updates.enabled() != existing.enabled() ? updates.enabled() : existing.enabled()
        );
        widgets.put(widgetId, updated);
        return updated;
    }

    public void removeWidget(String widgetId) {
        log.debug("Removing widget: {}", widgetId);
        if (widgets.remove(widgetId) == null) {
            throw new IllegalArgumentException("Widget not found: " + widgetId);
        }
        dashboards.values().forEach(dc -> {
            List<String> updatedIds = new ArrayList<>(dc.widgetIds());
            updatedIds.remove(widgetId);
        });
    }

    public Map<String, Object> getWidgetData(String widgetId) {
        log.debug("Fetching data for widget: {}", widgetId);
        DashboardWidget widget = widgets.get(widgetId);
        if (widget == null) {
            throw new IllegalArgumentException("Widget not found: " + widgetId);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("widgetId", widget.widgetId());
        data.put("title", widget.title());
        data.put("widgetType", widget.widgetType());
        data.put("metric", widget.metric());
        data.put("fetchedAt", OffsetDateTime.now().toString());
        data.put("data", generateWidgetData(widget));
        return data;
    }

    public DashboardResponse getExecutiveDashboard() {
        return getDashboard("DASH_EXEC");
    }

    public DashboardResponse getOperationsDashboard() {
        return getDashboard("DASH_OPS");
    }

    public DashboardResponse getMarketingDashboard() {
        return getDashboard("DASH_MKTG");
    }

    public DashboardResponse refreshDashboard(String dashboardId) {
        log.debug("Refreshing dashboard: {}", dashboardId);
        DashboardConfig config = dashboards.get(dashboardId);
        if (config == null) {
            throw new IllegalArgumentException("Dashboard not found: " + dashboardId);
        }
        DashboardConfig refreshed = new DashboardConfig(
                config.dashboardId(), config.name(), config.description(), config.type(),
                config.widgetIds(), config.layout(), OffsetDateTime.now()
        );
        dashboards.put(dashboardId, refreshed);
        return toResponse(refreshed);
    }

    private DashboardResponse toResponse(DashboardConfig config) {
        List<DashboardWidget> ws = config.widgetIds().stream()
                .map(widgets::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new DashboardResponse(
                config.dashboardId(), config.name(), config.description(), config.type(),
                ws, config.layout(), config.lastRefreshedAt(), OffsetDateTime.now()
        );
    }

    private Object generateWidgetData(DashboardWidget widget) {
        return switch (widget.widgetType()) {
            case "kpi_card" -> generateKpiData(widget);
            case "line_chart" -> generateLineChartData(widget);
            case "bar_chart" -> generateBarChartData(widget);
            case "pie_chart", "donut_chart" -> generatePieChartData(widget);
            case "gauge" -> generateGaugeData(widget);
            case "table" -> generateTableData(widget);
            case "list" -> generateListData(widget);
            case "scatter_plot" -> generateScatterData(widget);
            default -> Map.of("message", "Unsupported widget type: " + widget.widgetType());
        };
    }

    private Map<String, Object> generateKpiData(DashboardWidget widget) {
        Random rng = new Random(widget.widgetId().hashCode());
        double value = switch (widget.metric()) {
            case "total_revenue" -> 2450000 + rng.nextGaussian() * 50000;
            case "active_customers" -> 2840 + rng.nextInt(-50, 50);
            case "active_batches" -> 12 + rng.nextInt(-2, 3);
            case "contamination_rate" -> 3.8 + rng.nextGaussian() * 0.3;
            case "pending_orders" -> 42 + rng.nextInt(-10, 15);
            case "new_customers" -> 320 + rng.nextInt(-20, 30);
            case "satisfaction_score" -> 4.2 + rng.nextGaussian() * 0.1;
            default -> 500 + rng.nextGaussian() * 100;
        };
        return Map.of("value", Math.round(value * 100.0) / 100.0, "unit", getUnit(widget.metric()),
                "trend", rng.nextBoolean() ? "up" : "down", "changePercent", rng.nextDouble() * 15 - 5);
    }

    private Map<String, Object> generateLineChartData(DashboardWidget widget) {
        Random rng = new Random(widget.widgetId().hashCode());
        List<Map<String, Object>> series = new ArrayList<>();
        int points = 12;
        for (int i = 0; i < points; i++) {
            series.add(Map.of("period", OffsetDateTime.now().minusMonths(points - 1 - i).getMonth().name(),
                    "value", 1000 + i * 150 + rng.nextGaussian() * 100));
        }
        return Map.of("series", List.of(Map.of("name", widget.metric(), "data", series)),
                "xAxis", "period", "yAxis", "value");
    }

    private Map<String, Object> generateBarChartData(DashboardWidget widget) {
        return Map.of("categories", List.of("Oyster", "Shiitake", "Button", "Lion's Mane", "Enoki"),
                "values", List.of(18000, 12000, 10000, 3000, 2000),
                "unit", "kg");
    }

    private Map<String, Object> generatePieChartData(DashboardWidget widget) {
        return Map.of("segments", List.of(
                Map.of("name", "Online", "value", 55.0),
                Map.of("name", "Retail", "value", 25.0),
                Map.of("name", "Distributor", "value", 12.0),
                Map.of("name", "Direct", "value", 8.0)
        ));
    }

    private Map<String, Object> generateGaugeData(DashboardWidget widget) {
        return Map.of("value", 78.4, "min", 0, "max", 100,
                "thresholds", Map.of("good", 80, "warning", 60, "danger", 40));
    }

    private Map<String, Object> generateTableData(DashboardWidget widget) {
        return Map.of("columns", List.of("Product", "Revenue", "Growth"),
                "rows", List.of(
                        List.of("Oyster Mushroom Spawn", "Rs.8,50,000", "+12%"),
                        List.of("Shiitake Spawn", "Rs.6,50,000", "+8%"),
                        List.of("Button Mushroom Spawn", "Rs.5,00,000", "+5%"),
                        List.of("Training Courses", "Rs.2,80,000", "+22%"),
                        List.of("Equipment", "Rs.1,70,000", "-3%")
                ));
    }

    private Map<String, Object> generateListData(DashboardWidget widget) {
        return Map.of("items", List.of(
                Map.of("type", "warning", "message", "Contamination rate elevated in South facility", "timestamp", OffsetDateTime.now().minusHours(2)),
                Map.of("type", "info", "message", "Training batch B-042 completed with 91% pass rate", "timestamp", OffsetDateTime.now().minusHours(5)),
                Map.of("type", "success", "message", "Oyster mushroom yield target achieved for Q2", "timestamp", OffsetDateTime.now().minusDays(1)),
                Map.of("type", "error", "message", "Inventory low for premium spawn SKU-108", "timestamp", OffsetDateTime.now().minusDays(1)),
                Map.of("type", "warning", "message", "Customer churn rate exceeded 15% in New Grower segment", "timestamp", OffsetDateTime.now().minusDays(2))
        ));
    }

    private Map<String, Object> generateScatterData(DashboardWidget widget) {
        Random rng = new Random(42);
        List<Map<String, Object>> points = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            points.add(Map.of("x", 50 + rng.nextGaussian() * 20, "y", 300 + rng.nextGaussian() * 80));
        }
        return Map.of("points", points, "xLabel", "Batch Size (kg)", "yLabel", "Yield (kg)");
    }

    private String getUnit(String metric) {
        return switch (metric) {
            case "total_revenue" -> "Rs.";
            case "contamination_rate" -> "%";
            case "satisfaction_score" -> "/5";
            case "completion_rate" -> "%";
            default -> "";
        };
    }
}
