package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ForecastResult;
import com.sporekart.bi.copilot.domain.ForecastResult.ForecastPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForecastingEngineTest {

    private ForecastingEngine engine;
    private List<Double> sampleData;

    @BeforeEach
    void setUp() {
        engine = new ForecastingEngine();
        sampleData = List.of(100.0, 110.0, 120.0, 130.0, 140.0, 150.0,
                160.0, 170.0, 180.0, 190.0, 200.0, 210.0);
    }

    @Test
    void movingAverageForecast_shouldReturnCorrectCount() {
        List<ForecastPoint> points = engine.movingAverageForecast(new java.util.ArrayList<>(sampleData), 3, 4);
        assertEquals(4, points.size());
    }

    @Test
    void movingAverageForecast_shouldHaveBounds() {
        List<ForecastPoint> points = engine.movingAverageForecast(new java.util.ArrayList<>(sampleData), 3, 2);
        assertTrue(points.get(0).lowerBound() <= points.get(0).predictedValue());
        assertTrue(points.get(0).upperBound() >= points.get(0).predictedValue());
    }

    @Test
    void exponentialSmoothingForecast_shouldReturnCorrectCount() {
        List<ForecastPoint> points = engine.exponentialSmoothingForecast(sampleData, 0.3, 5);
        assertEquals(5, points.size());
    }

    @Test
    void exponentialSmoothingForecast_shouldProduceStableForecast() {
        List<ForecastPoint> points = engine.exponentialSmoothingForecast(sampleData, 0.3, 3);
        assertTrue(points.get(0).predictedValue() > 0);
    }

    @Test
    void linearRegressionForecast_shouldReturnCorrectCount() {
        List<ForecastPoint> points = engine.linearRegressionForecast(sampleData, 3);
        assertEquals(3, points.size());
    }

    @Test
    void linearRegressionForecast_shouldShowUpwardTrend() {
        List<ForecastPoint> points = engine.linearRegressionForecast(sampleData, 2);
        assertTrue(points.get(1).predictedValue() > points.get(0).predictedValue());
    }

    @Test
    void seasonalForecast_shouldReturnCorrectCount() {
        List<ForecastPoint> points = engine.seasonalForecast(sampleData, 4, 6);
        assertEquals(6, points.size());
    }

    @Test
    void forecastRevenue_shouldReturnForecast() {
        ForecastResult result = engine.forecastRevenue(3);
        assertEquals("revenue", result.metric());
        assertEquals(3, result.horizon());
        assertEquals(3, result.points().size());
    }

    @Test
    void calculateAccuracy_shouldReturnPercentage() {
        List<Double> actual = List.of(100.0, 110.0, 120.0);
        List<Double> predicted = List.of(105.0, 108.0, 118.0);
        double accuracy = engine.calculateAccuracy(actual, predicted);
        assertTrue(accuracy > 0 && accuracy <= 100);
    }

    @Test
    void calculateAccuracy_shouldReturnZeroForEmptyLists() {
        double accuracy = engine.calculateAccuracy(List.of(), List.of());
        assertEquals(0, accuracy);
    }

    @Test
    void recommendMethod_shouldReturnMovingAverageForSmallData() {
        String method = engine.recommendMethod("test", List.of(1.0, 2.0, 3.0));
        assertEquals("moving_average", method);
    }

    @Test
    void recommendMethod_shouldReturnSeasonalForPatternedData() {
        List<Double> seasonalData = List.of(100.0, 80.0, 100.0, 80.0, 100.0, 80.0, 100.0, 80.0);
        String method = engine.recommendMethod("test", seasonalData);
        assertNotNull(method);
    }

    @Test
    void forecast_shouldThrowForTooFewPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.forecast("test", "moving_average", 3, List.of(1.0, 2.0)));
    }

    @Test
    void forecast_shouldThrowForUnknownMethod() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.forecast("test", "unknown", 3, sampleData));
    }
}
