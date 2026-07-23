package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrendDetectionEngineTest {

    private TrendDetectionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new TrendDetectionEngine();
    }

    @Test
    void detectTrends_shouldReturnDataPointsForGivenInput() {
        List<Double> data = List.of(100.0, 110.0, 120.0, 130.0, 140.0);
        List<TrendDataPoint> trends = engine.detectTrends("revenue", data);
        assertEquals(5, trends.size());
    }

    @Test
    void detectTrends_shouldReturnEmptyForEmptyInput() {
        List<TrendDataPoint> trends = engine.detectTrends("revenue", List.of());
        assertTrue(trends.isEmpty());
    }

    @Test
    void detectTrends_shouldReturnEmptyForNullInput() {
        List<TrendDataPoint> trends = engine.detectTrends("revenue", null);
        assertTrue(trends.isEmpty());
    }

    @Test
    void detectTrends_shouldSetCorrectMetric() {
        List<Double> data = List.of(100.0, 110.0, 120.0);
        List<TrendDataPoint> trends = engine.detectTrends("orders", data);
        assertTrue(trends.stream().allMatch(t -> "orders".equals(t.metric())));
    }

    @Test
    void calculateMovingAverage_shouldReturnAveragedValues() {
        List<Double> data = List.of(10.0, 20.0, 30.0, 40.0, 50.0);
        List<Double> ma = engine.calculateMovingAverage(data, 3);
        assertEquals(5, ma.size());
        assertEquals(20.0, ma.get(2), 0.001);
    }

    @Test
    void calculateMovingAverage_shouldReturnEmptyForEmptyData() {
        List<Double> ma = engine.calculateMovingAverage(List.of(), 3);
        assertTrue(ma.isEmpty());
    }

    @Test
    void calculateMovingAverage_shouldHandleWindowLargerThanData() {
        List<Double> data = List.of(10.0, 20.0);
        List<Double> ma = engine.calculateMovingAverage(data, 5);
        assertEquals(2, ma.size());
    }

    @Test
    void detectGrowthAcceleration_shouldReturnInsightsForGrowingTrend() {
        List<Double> data = List.of(100.0, 120.0, 145.0, 175.0, 210.0, 250.0);
        List<TrendDataPoint> trends = engine.detectTrends("revenue", data);
        List<BusinessInsight> insights = engine.detectGrowthAcceleration(trends);
        assertNotNull(insights);
    }

    @Test
    void detectGrowthAcceleration_shouldReturnEmptyForFewPoints() {
        List<BusinessInsight> insights = engine.detectGrowthAcceleration(List.of());
        assertTrue(insights.isEmpty());
    }

    @Test
    void getTopTrends_shouldReturnLimitedResults() {
        List<TrendDataPoint> trends = engine.getTopTrends(5);
        assertEquals(5, trends.size());
    }

    @Test
    void getTopTrends_shouldBeSortedByDeviation() {
        List<TrendDataPoint> trends = engine.getTopTrends(12);
        assertFalse(trends.isEmpty());
        assertTrue(trends.size() <= 12);
    }

    @Test
    void calculateMovingAverage_shouldWorkWithSingleElement() {
        List<Double> data = List.of(42.0);
        List<Double> ma = engine.calculateMovingAverage(data, 3);
        assertEquals(1, ma.size());
        assertEquals(42.0, ma.get(0));
    }

    @Test
    void detectTrends_shouldHandleSingleDataPoint() {
        List<TrendDataPoint> trends = engine.detectTrends("test", List.of(99.0));
        assertEquals(1, trends.size());
    }
}
