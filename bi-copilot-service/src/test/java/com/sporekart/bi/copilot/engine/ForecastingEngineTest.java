package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessForecast;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForecastingEngineTest {

    @InjectMocks
    private ForecastingEngine engine;

    @Test
    void autoForecastReturnsForecastForRevenue() {
        BusinessForecast result = engine.autoForecast("revenue", "current", 6);
        assertNotNull(result);
        assertEquals("revenue", result.metric());
        assertNotNull(result.points());
        assertFalse(result.points().isEmpty());
        assertTrue(result.confidenceInterval() > 0);
    }

    @Test
    void autoForecastReturnsForecastForOrders() {
        BusinessForecast result = engine.autoForecast("orders", "current", 6);
        assertNotNull(result);
        assertEquals("orders", result.metric());
    }

    @Test
    void autoForecastReturnsForecastForCustomerGrowth() {
        BusinessForecast result = engine.autoForecast("customerGrowth", "current", 6);
        assertNotNull(result);
        assertEquals("customerGrowth", result.metric());
    }

    @Test
    void autoForecastReturnsForecastForTraining() {
        BusinessForecast result = engine.autoForecast("training", "current", 6);
        assertNotNull(result);
        assertEquals("training", result.metric());
    }

    @Test
    void movingAverageForecastReturnsForecast() {
        BusinessForecast result = engine.movingAverageForecast("revenue", "current", 6, 3);
        assertNotNull(result);
        assertEquals("moving_average", result.method());
    }

    @Test
    void exponentialSmoothingReturnsForecast() {
        BusinessForecast result = engine.exponentialSmoothing("revenue", "current", 6, 0.3);
        assertNotNull(result);
        assertEquals("exponential_smoothing", result.method());
    }

    @Test
    void linearRegressionReturnsForecast() {
        BusinessForecast result = engine.linearRegression("revenue", "current", 6);
        assertNotNull(result);
        assertEquals("linear_regression", result.method());
    }

    @Test
    void seasonalReturnsForecast() {
        BusinessForecast result = engine.seasonal("revenue", "current", 6, 12);
        assertNotNull(result);
        assertEquals("seasonal", result.method());
    }

    @Test
    void calculateAccuracyReturnsAccuracyScore() {
        double accuracy = engine.calculateAccuracy("revenue", "current", "auto");
        assertTrue(accuracy >= 0);
        assertTrue(accuracy <= 100);
    }

    @Test
    void detectSeasonalityReturnsSeasonalityDescription() {
        String seasonality = engine.detectSeasonality("revenue");
        assertNotNull(seasonality);
        assertFalse(seasonality.isBlank());
    }

    @Test
    void detectTrendReturnsTrendDescription() {
        String trend = engine.detectTrend("revenue");
        assertNotNull(trend);
        assertFalse(trend.isBlank());
    }

    @Test
    void autoForecastReturnsRecommendations() {
        BusinessForecast result = engine.autoForecast("revenue", "current", 6);
        assertNotNull(result.recommendations());
        assertFalse(result.recommendations().isBlank());
    }
}
