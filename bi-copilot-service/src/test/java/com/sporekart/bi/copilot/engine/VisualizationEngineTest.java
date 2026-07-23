package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.VisualizationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VisualizationEngineTest {

    @InjectMocks
    private VisualizationEngine engine;

    @Test
    void generateLineChartReturnsLineChartConfig() {
        VisualizationConfig viz = engine.generateLineChart(
                "Revenue Trend",
                List.of("Jan", "Feb", "Mar"),
                List.of(100.0, 150.0, 200.0));
        assertNotNull(viz);
        assertEquals("line", viz.type());
        assertEquals("Revenue Trend", viz.title());
        assertEquals(3, viz.labels().size());
        assertEquals(3, viz.values().size());
    }

    @Test
    void generateBarChartReturnsBarChartConfig() {
        VisualizationConfig viz = engine.generateBarChart(
                "Revenue by Category",
                List.of("Mushrooms", "Training", "Equipment"),
                List.of(50000.0, 30000.0, 20000.0));
        assertNotNull(viz);
        assertEquals("bar", viz.type());
    }

    @Test
    void generatePieChartReturnsPieChartConfig() {
        VisualizationConfig viz = engine.generatePieChart(
                "Customer Segments",
                List.of("Home Growers", "Commercial"),
                List.of(60.0, 40.0));
        assertNotNull(viz);
        assertEquals("pie", viz.type());
        assertNotNull(viz.colors());
        assertFalse(viz.colors().isEmpty());
    }

    @Test
    void generateKpiReturnsKpiConfig() {
        VisualizationConfig viz = engine.generateKpi(
                "Total Revenue", 250000.0, 220000.0, 13.6);
        assertNotNull(viz);
        assertEquals("kpi", viz.type());
        assertTrue(viz.options().containsKey("value"));
        assertTrue(viz.options().containsKey("previousValue"));
        assertTrue(viz.options().containsKey("changePercent"));
    }

    @Test
    void generateForecastGraphReturnsForecastConfig() {
        VisualizationConfig viz = engine.generateForecastGraph(
                "Sales Forecast",
                List.of("Jun", "Jul", "Aug", "Sep"),
                List.of(200.0, 220.0, 215.0, 230.0),
                List.of(180.0, 200.0, 195.0, 210.0),
                List.of(220.0, 240.0, 235.0, 250.0));
        assertNotNull(viz);
        assertEquals("forecast", viz.type());
        assertTrue(viz.options().containsKey("lowerBound"));
        assertTrue(viz.options().containsKey("upperBound"));
    }

    @Test
    void autoVisualizeReturnsLineChartForTrendIntent() {
        VisualizationConfig viz = engine.autoVisualize(
                Map.of("Jan", 100.0, "Feb", 150.0, "Mar", 200.0),
                "trend");
        assertNotNull(viz);
        assertEquals("line", viz.type());
    }

    @Test
    void autoVisualizeReturnsBarChartForComparisonIntent() {
        VisualizationConfig viz = engine.autoVisualize(
                Map.of("Product A", 100.0, "Product B", 200.0),
                "comparison");
        assertNotNull(viz);
        assertEquals("bar", viz.type());
    }

    @Test
    void autoVisualizeReturnsPieChartForDistributionIntent() {
        VisualizationConfig viz = engine.autoVisualize(
                Map.of("Segment A", 60.0, "Segment B", 40.0),
                "distribution");
        assertNotNull(viz);
        assertEquals("pie", viz.type());
    }

    @Test
    void autoVisualizeReturnsKpiForSingleValueIntent() {
        VisualizationConfig viz = engine.autoVisualize(
                Map.of("value", 250000.0),
                "kpi");
        assertNotNull(viz);
        assertEquals("kpi", viz.type());
    }
}
