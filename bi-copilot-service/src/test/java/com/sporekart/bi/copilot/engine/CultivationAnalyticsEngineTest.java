package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CultivationAnalytics;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CultivationAnalyticsEngineTest {

    private CultivationAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CultivationAnalyticsEngine();
    }

    @Test
    void getCultivationSummary_shouldReturnAnalyticsForValidPeriod() {
        CultivationAnalytics analytics = engine.getCultivationSummary("2025-06");
        assertNotNull(analytics);
        assertEquals("2025-06", analytics.period());
        assertTrue(analytics.totalYieldKg() > 0);
    }

    @Test
    void getCultivationSummary_shouldReturnLatestForNullPeriod() {
        CultivationAnalytics analytics = engine.getCultivationSummary(null);
        assertNotNull(analytics);
        assertTrue(analytics.totalYieldKg() > 0);
    }

    @Test
    void getCultivationSummary_shouldHavePositiveGrowers() {
        CultivationAnalytics analytics = engine.getCultivationSummary("2025-06");
        assertTrue(analytics.totalGrowers() > 0);
    }

    @Test
    void getYieldBySpecies_shouldReturnFiveSpecies() {
        Map<String, Double> yields = engine.getYieldBySpecies("2025-06");
        assertEquals(5, yields.size());
    }

    @Test
    void getYieldBySpecies_shouldReturnEmptyForUnknownPeriod() {
        Map<String, Double> yields = engine.getYieldBySpecies("2099-01");
        assertTrue(yields.isEmpty());
    }

    @Test
    void getYieldByRegion_shouldReturnFiveRegions() {
        Map<String, Double> yields = engine.getYieldByRegion("2025-06");
        assertEquals(5, yields.size());
    }

    @Test
    void getContaminationRateTrend_shouldReturnRequestedMonths() {
        List<TrendDataPoint> trend = engine.getContaminationRateTrend(4);
        assertEquals(4, trend.size());
    }

    @Test
    void getContaminationRateTrend_shouldHaveContaminationMetric() {
        List<TrendDataPoint> trend = engine.getContaminationRateTrend(3);
        assertTrue(trend.stream().allMatch(t -> "contamination_rate".equals(t.metric())));
    }

    @Test
    void getTopGrowers_shouldReturnLimitedResults() {
        List<Map<String, Object>> growers = engine.getTopGrowers(5);
        assertEquals(5, growers.size());
    }

    @Test
    void getTopGrowers_shouldBeSortedByRevenue() {
        List<Map<String, Object>> growers = engine.getTopGrowers(3);
        assertTrue((Double) growers.get(0).get("revenue") >= (Double) growers.get(1).get("revenue"));
    }

    @Test
    void getDiseaseIncidenceRate_shouldReturnFiveDiseases() {
        Map<String, Double> incidence = engine.getDiseaseIncidenceRate();
        assertEquals(5, incidence.size());
    }

    @Test
    void getAverageCycleTime_shouldReturnPositive() {
        double cycle = engine.getAverageCycleTime("Oyster");
        assertTrue(cycle > 0);
    }

    @Test
    void getAverageCycleTime_shouldReturnAverageForNullSpecies() {
        double cycle = engine.getAverageCycleTime(null);
        assertTrue(cycle > 0);
    }

    @Test
    void getGrowerSatisfaction_shouldReturnBetweenOneAndFive() {
        double satisfaction = engine.getGrowerSatisfaction();
        assertTrue(satisfaction >= 1 && satisfaction <= 5);
    }
}
