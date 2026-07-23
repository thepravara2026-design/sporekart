package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessForecast;
import com.sporekart.bi.copilot.domain.BusinessForecast.ForecastPoint;
import com.sporekart.bi.copilot.domain.VisualizationConfig;
import com.sporekart.bi.copilot.dto.KpiResponse.KpiEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class VisualizationEngine {

    private static final Logger log = LoggerFactory.getLogger(VisualizationEngine.class);

    private static final List<String> DEFAULT_COLORS = List.of(
            "#2563eb", "#10b981", "#f59e0b", "#ef4444", "#8b5cf6",
            "#06b6d4", "#ec4899", "#84cc16", "#f97316", "#6366f1"
    );

    public VisualizationConfig generateVisualization(String type, String title, List<String> labels, List<Double> values) {
        return switch (type.toLowerCase()) {
            case "line" -> generateLineChart(title, labels, values);
            case "bar" -> generateBarChart(title, labels, values);
            case "pie" -> generatePieChart(title, labels, values);
            case "kpi" -> generateKpi(title, values.isEmpty() ? 0 : values.getFirst(), values.size() > 1 ? values.get(1) : 0);
            default -> generateBarChart(title, labels, values);
        };
    }

    public VisualizationConfig generateLineChart(String title, List<String> labels, List<Double> values) {
        return buildViz("line", title, labels, values,
                Map.of("showLegend", true, "showGrid", true, "interpolate", "monotone",
                        "xAxis", "Period", "yAxis", "Value"));
    }

    public VisualizationConfig generateBarChart(String title, List<String> labels, List<Double> values) {
        return buildViz("bar", title, labels, values,
                Map.of("showLegend", true, "horizontal", false, "stacked", false,
                        "xAxis", "Category", "yAxis", "Value"));
    }

    public VisualizationConfig generatePieChart(String title, List<String> labels, List<Double> values) {
        return buildViz("pie", title, labels, values,
                Map.of("showLegend", true, "donut", false, "showPercentages", true));
    }

    public VisualizationConfig generateHeatMap(String title, List<String> rowLabels, List<String> colLabels,
                                                List<List<Double>> values) {
        Map<String, Object> options = Map.of(
                "type", "heatmap",
                "rowLabels", rowLabels,
                "colLabels", colLabels,
                "colorScale", "Viridis",
                "showValues", true
        );
        List<Double> flatValues = values.stream().flatMap(Collection::stream).toList();
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "heatmap", title, "HeatMap",
                options, rowLabels, flatValues, DEFAULT_COLORS,
                Map.of("rows", rowLabels.size(), "cols", colLabels.size())
        );
    }

    public VisualizationConfig generateTrendGraph(String title, List<String> labels, List<Double> values,
                                                    List<Double> trendLine) {
        Map<String, Object> options = Map.of(
                "type", "trend",
                "showLegend", true,
                "showGrid", true,
                "xAxis", "Period",
                "yAxis", "Value",
                "series", List.of(
                        Map.of("name", "Actual", "data", values),
                        Map.of("name", "Trend", "data", trendLine, "dashed", true)
                )
        );
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "trend", title, "TrendAnalysis",
                options, labels, values, DEFAULT_COLORS,
                Map.of("hasTrendLine", true)
        );
    }

    public VisualizationConfig generateForecastGraph(BusinessForecast forecast) {
        List<String> labels = forecast.points().stream().map(ForecastPoint::period).toList();
        List<Double> values = forecast.points().stream().map(ForecastPoint::value).toList();
        List<Double> lowerBounds = forecast.points().stream().map(ForecastPoint::lowerBound).toList();
        List<Double> upperBounds = forecast.points().stream().map(ForecastPoint::upperBound).toList();

        Map<String, Object> options = Map.of(
                "type", "forecast",
                "metric", forecast.metric(),
                "method", forecast.method(),
                "accuracy", forecast.accuracy(),
                "confidenceInterval", forecast.confidenceInterval(),
                "lowerBounds", lowerBounds,
                "upperBounds", upperBounds,
                "showConfidenceBand", true
        );
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "forecast", "Forecast: " + forecast.metric(),
                "ForecastingEngine", options, labels, values, DEFAULT_COLORS,
                Map.of("forecastId", forecast.forecastId(), "period", forecast.period(),
                        "seasonality", forecast.seasonality(), "trend", forecast.trend())
        );
    }

    public VisualizationConfig generateKpi(String title, double value, double previousValue) {
        double change = previousValue != 0 ? ((value - previousValue) / previousValue) * 100 : 0;
        String trend = change > 0 ? "UP" : change < 0 ? "DOWN" : "STABLE";

        Map<String, Object> options = Map.of(
                "type", "kpi",
                "value", value,
                "previousValue", previousValue,
                "changePercent", change,
                "trend", trend,
                "unit", ""
        );
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "kpi", title, "KPI",
                options, List.of(title), List.of(value, previousValue), DEFAULT_COLORS,
                Map.of("trend", trend, "changePercent", change)
        );
    }

    public VisualizationConfig generateScorecard(String title, List<KpiEntry> kpis) {
        List<String> labels = kpis.stream().map(KpiEntry::name).toList();
        List<Double> values = kpis.stream().map(KpiEntry::value).toList();
        Map<String, Object> options = Map.of(
                "type", "scorecard",
                "kpis", kpis.stream().map(k -> Map.<String, Object>of(
                        "id", k.id(), "name", k.name(), "value", k.value(),
                        "previousValue", k.previousValue(), "changePercent", k.changePercent(),
                        "trend", k.trend(), "unit", k.unit(), "status", k.status()
                )).toList()
        );
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "scorecard", title, "Scorecard",
                options, labels, values, DEFAULT_COLORS,
                Map.of("kpiCount", kpis.size())
        );
    }

    public VisualizationConfig generateTable(String title, List<Map<String, Object>> rows) {
        Set<String> columns = rows.isEmpty() ? Set.of() : rows.getFirst().keySet();
        Map<String, Object> options = Map.of(
                "type", "table",
                "columns", List.copyOf(columns),
                "rows", rows,
                "pageSize", 20,
                "sortable", true
        );
        List<String> labels = new ArrayList<>(columns);
        List<Double> values = rows.stream()
                .flatMap(r -> r.values().stream())
                .filter(Number.class::isInstance)
                .map(v -> ((Number) v).doubleValue())
                .toList();
        return new VisualizationConfig(
                UUID.randomUUID().toString(), "table", title, "Table",
                options, labels, values, DEFAULT_COLORS,
                Map.of("rowCount", rows.size(), "columnCount", columns.size())
        );
    }

    public VisualizationConfig autoVisualize(String intent, Map<String, Object> data) {
        String vizType = switch (intent.toUpperCase()) {
            case "COMPARISON" -> "bar";
            case "TREND", "FORECAST" -> "line";
            case "EXPLANATION" -> "pie";
            case "RECOMMENDATION" -> "scorecard";
            default -> {
                Object values = data.get("values");
                yield (values instanceof Map<?, ?> m && m.size() <= 2) ? "kpi" : "bar";
            }
        };

        List<String> labels = data.containsKey("labels")
                ? ((List<?>) data.get("labels")).stream().map(Object::toString).toList()
                : List.of();
        List<Double> values = data.containsKey("values")
                ? extractDoubles(data.get("values"))
                : List.of();

        return generateVisualization(vizType, "Analytics: " + intent, labels, values);
    }

    private VisualizationConfig buildViz(String type, String title, List<String> labels,
                                          List<Double> values, Map<String, Object> options) {
        return new VisualizationConfig(
                UUID.randomUUID().toString(), type, title, "VisualizationEngine",
                options, labels, values, DEFAULT_COLORS,
                Map.of("dataPoints", values.size())
        );
    }

    @SuppressWarnings("unchecked")
    private List<Double> extractDoubles(Object value) {
        if (value instanceof List<?> list) {
            return list.stream()
                    .filter(Number.class::isInstance)
                    .map(n -> ((Number) n).doubleValue())
                    .toList();
        }
        if (value instanceof Map<?, ?> map) {
            return map.values().stream()
                    .filter(Number.class::isInstance)
                    .map(n -> ((Number) n).doubleValue())
                    .toList();
        }
        return List.of();
    }
}
