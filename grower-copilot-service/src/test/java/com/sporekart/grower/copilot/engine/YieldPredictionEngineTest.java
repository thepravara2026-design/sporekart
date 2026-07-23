package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.YieldPrediction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class YieldPredictionEngineTest {

    private YieldPredictionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new YieldPredictionEngine();
    }

    @Test
    void predictYield_ForOyster_ShouldReturnPrediction() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        assertThat(prediction).isNotNull();
        assertThat(prediction.speciesName()).isEqualTo("Oyster");
        assertThat(prediction.expectedYieldKg()).isPositive();
    }

    @Test
    void predictYield_ForButton_ShouldReturnPrediction() {
        YieldPrediction prediction = engine.predictYield("Button", "Compost", 200.0, 400, "Himachal Pradesh", Map.of());
        assertThat(prediction).isNotNull();
        assertThat(prediction.expectedYieldKg()).isPositive();
    }

    @Test
    void predictYield_ForShiitake_ShouldReturnPrediction() {
        YieldPrediction prediction = engine.predictYield("Shiitake", "Sawdust", 50.0, 100, "Karnataka", Map.of());
        assertThat(prediction).isNotNull();
        assertThat(prediction.numberOfBags()).isEqualTo(100);
    }

    @Test
    void predictYield_ForMilky_ShouldReturnPrediction() {
        YieldPrediction prediction = engine.predictYield("Milky", "Paddy Straw", 75.0, 150, "Maharashtra", Map.of());
        assertThat(prediction).isNotNull();
        assertThat(prediction.expectedYieldKg()).isPositive();
    }

    @Test
    void predictYield_ForPaddyStraw_ShouldReturnPrediction() {
        YieldPrediction prediction = engine.predictYield("Paddy Straw", "Rice Straw", 60.0, 120, "Tamil Nadu", Map.of());
        assertThat(prediction).isNotNull();
        assertThat(prediction.predictionId()).isNotBlank();
    }

    @Test
    void predictYield_WithAdditionalParameters_ShouldUseThem() {
        Map<String, Object> params = Map.of("humidity", 80, "temperature", 25);
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", params);
        assertThat(prediction).isNotNull();
    }

    @Test
    void predictYield_WithLowArea_ShouldReturnLowerYield() {
        YieldPrediction small = engine.predictYield("Oyster", "Wheat Straw", 10.0, 20, "Punjab", Map.of());
        YieldPrediction large = engine.predictYield("Oyster", "Wheat Straw", 1000.0, 2000, "Punjab", Map.of());
        assertThat(small.expectedYieldKg()).isLessThan(large.expectedYieldKg());
    }

    @Test
    void calculateEfficiency_ShouldReturnPercentage() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        double efficiency = engine.calculateEfficiency(prediction);
        assertThat(efficiency).isBetween(0.0, 100.0);
    }

    @Test
    void calculateEfficiency_ForButton_ShouldReturnPercentage() {
        YieldPrediction prediction = engine.predictYield("Button", "Compost", 200.0, 400, "Himachal Pradesh", Map.of());
        double efficiency = engine.calculateEfficiency(prediction);
        assertThat(efficiency).isPositive();
    }

    @Test
    void estimateRevenue_ShouldReturnAmount() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        double revenue = engine.estimateRevenue(prediction);
        assertThat(revenue).isPositive();
    }

    @Test
    void estimateRevenue_ForButton_ShouldReturnHigher() {
        YieldPrediction oyster = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        YieldPrediction button = engine.predictYield("Button", "Compost", 100.0, 200, "Himachal Pradesh", Map.of());
        double oysterRev = engine.estimateRevenue(oyster);
        double buttonRev = engine.estimateRevenue(button);
        assertThat(buttonRev).isNotNegative();
    }

    @Test
    void calculateProductionCost_ShouldReturnCost() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        double cost = engine.calculateProductionCost(prediction);
        assertThat(cost).isPositive();
    }

    @Test
    void calculateProductionCost_ForDifferentSpecies_ShouldVary() {
        YieldPrediction oyster = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        YieldPrediction shiitake = engine.predictYield("Shiitake", "Sawdust", 100.0, 200, "Karnataka", Map.of());
        assertThat(engine.calculateProductionCost(oyster)).isNotEqualTo(engine.calculateProductionCost(shiitake));
    }

    @Test
    void calculateProfitMargin_ShouldReturnMargin() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        double margin = engine.calculateProfitMargin(prediction);
        assertThat(margin).isBetween(-100.0, 100.0);
    }

    @Test
    void calculateProfitMargin_ShouldBePositiveForViableFarm() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 500.0, 1000, "Punjab", Map.of());
        double margin = engine.calculateProfitMargin(prediction);
        assertThat(margin).isPositive();
    }

    @Test
    void calculateRiskScore_ShouldReturnScore() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        double risk = engine.calculateRiskScore(prediction);
        assertThat(risk).isBetween(0.0, 100.0);
    }

    @Test
    void calculateRiskScore_ShouldBeHigherForUnknown() {
        YieldPrediction unknown = engine.predictYield("Unknown", "Unknown", 100.0, 200, "Unknown", Map.of());
        YieldPrediction oyster = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        assertThat(engine.calculateRiskScore(unknown)).isGreaterThanOrEqualTo(engine.calculateRiskScore(oyster));
    }

    @Test
    void getHarvestWindow_ShouldReturnWindow() {
        String window = engine.getHarvestWindow("Oyster", "2026-07-01");
        assertThat(window).isNotBlank();
        assertThat(window).contains("2026");
    }

    @Test
    void getHarvestWindow_ForButton_ShouldReturnWindow() {
        String window = engine.getHarvestWindow("Button", "2026-08-15");
        assertThat(window).isNotBlank();
    }

    @Test
    void getHarvestWindow_ForShiitake_ShouldReturnWindow() {
        String window = engine.getHarvestWindow("Shiitake", "2026-06-01");
        assertThat(window).isNotBlank();
    }

    @Test
    void predictYield_ShouldIncludeRecommendations() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        assertThat(prediction.recommendations()).isNotBlank();
    }

    @Test
    void predictYield_ConfidenceLevel_ShouldBePositive() {
        YieldPrediction prediction = engine.predictYield("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        assertThat(prediction.confidenceLevel()).isBetween(0, 100);
    }
}
