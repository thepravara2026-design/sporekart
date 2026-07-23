package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.BusinessPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BusinessPlanningEngineTest {

    private BusinessPlanningEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BusinessPlanningEngine();
    }

    @Test
    void createBusinessPlan_ForOyster_ShouldReturnPlan() {
        BusinessPlan plan = engine.createBusinessPlan("Oyster", 500.0, 200000.0, "beginner", "Punjab");
        assertThat(plan).isNotNull();
        assertThat(plan.speciesName()).isEqualTo("Oyster");
        assertThat(plan.investmentAmount()).isEqualTo(200000.0);
    }

    @Test
    void createBusinessPlan_ForButton_ShouldReturnPlan() {
        BusinessPlan plan = engine.createBusinessPlan("Button", 1000.0, 500000.0, "intermediate", "Himachal Pradesh");
        assertThat(plan).isNotNull();
        assertThat(plan.investmentAmount()).isEqualTo(500000.0);
    }

    @Test
    void createBusinessPlan_ForShiitake_ShouldReturnPlan() {
        BusinessPlan plan = engine.createBusinessPlan("Shiitake", 200.0, 300000.0, "expert", "Karnataka");
        assertThat(plan).isNotNull();
        assertThat(plan.speciesName()).isEqualTo("Shiitake");
    }

    @Test
    void createBusinessPlan_ForMilky_ShouldReturnPlan() {
        BusinessPlan plan = engine.createBusinessPlan("Milky", 300.0, 150000.0, "beginner", "Maharashtra");
        assertThat(plan).isNotNull();
        assertThat(plan.expectedRevenue()).isPositive();
    }

    @Test
    void createBusinessPlan_ForPaddyStraw_ShouldReturnPlan() {
        BusinessPlan plan = engine.createBusinessPlan("Paddy Straw", 400.0, 100000.0, "beginner", "Tamil Nadu");
        assertThat(plan).isNotNull();
        assertThat(plan.breakEvenPoint()).isPositive();
    }

    @Test
    void createBusinessPlan_WithNullLocation_ShouldUseDefault() {
        BusinessPlan plan = engine.createBusinessPlan("Oyster", 500.0, 200000.0, "beginner", null);
        assertThat(plan).isNotNull();
    }

    @Test
    void estimateInvestment_ShouldReturnBreakdown() {
        Map<String, Object> investment = engine.estimateInvestment("Oyster", 500.0, "Punjab");
        assertThat(investment).isNotNull();
        assertThat(investment).containsKey("total");
    }

    @Test
    void estimateInvestment_ForButton_ShouldReturnHigher() {
        Map<String, Object> oysterInv = engine.estimateInvestment("Oyster", 500.0, "Punjab");
        Map<String, Object> buttonInv = engine.estimateInvestment("Button", 500.0, "Himachal Pradesh");
        assertThat(oysterInv).isNotNull();
        assertThat(buttonInv).isNotNull();
    }

    @Test
    void calculateROI_ShouldReturnPercentage() {
        double roi = engine.calculateROI("Oyster", 200000.0, 500.0, "Punjab");
        assertThat(roi).isBetween(-100.0, 500.0);
    }

    @Test
    void calculateROI_ForButton_ShouldReturnDifferent() {
        double oysterRoi = engine.calculateROI("Oyster", 200000.0, 500.0, "Punjab");
        double buttonRoi = engine.calculateROI("Button", 500000.0, 1000.0, "Himachal Pradesh");
        assertThat(oysterRoi).isNotZero();
        assertThat(buttonRoi).isNotZero();
    }

    @Test
    void getMarketInsights_ShouldReturnInsights() {
        List<String> insights = engine.getMarketInsights("Oyster", "Punjab");
        assertThat(insights).isNotEmpty();
    }

    @Test
    void getMarketInsights_ForButton_ShouldReturnInsights() {
        List<String> insights = engine.getMarketInsights("Button", "Himachal Pradesh");
        assertThat(insights).isNotEmpty();
    }

    @Test
    void getMarketInsights_ForShiitake_ShouldReturnInsights() {
        List<String> insights = engine.getMarketInsights("Shiitake", "Karnataka");
        assertThat(insights).isNotEmpty();
    }

    @Test
    void getSeasonalDemand_ShouldReturnDemand() {
        List<String> demand = engine.getSeasonalDemand("Oyster");
        assertThat(demand).isNotEmpty();
    }

    @Test
    void getSeasonalDemand_ForButton_ShouldReturnDemand() {
        List<String> demand = engine.getSeasonalDemand("Button");
        assertThat(demand).isNotEmpty();
    }

    @Test
    void getPricingSuggestions_ShouldReturnSuggestions() {
        List<String> suggestions = engine.getPricingSuggestions("Oyster", "Punjab");
        assertThat(suggestions).isNotEmpty();
    }

    @Test
    void getPricingSuggestions_ForButton_ShouldReturnSuggestions() {
        List<String> suggestions = engine.getPricingSuggestions("Button", "Himachal Pradesh");
        assertThat(suggestions).isNotEmpty();
    }

    @Test
    void getPackagingAdvice_ShouldReturnAdvice() {
        String advice = engine.getPackagingAdvice("Oyster");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getPackagingAdvice_ForButton_ShouldReturnAdvice() {
        String advice = engine.getPackagingAdvice("Button");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getStorageAdvice_ShouldReturnAdvice() {
        String advice = engine.getStorageAdvice("Oyster");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getStorageAdvice_ForButton_ShouldReturnAdvice() {
        String advice = engine.getStorageAdvice("Button");
        assertThat(advice).isNotBlank();
    }

    @Test
    void generateProductionPlan_ShouldReturnPlan() {
        List<BusinessPlan.ProductionPlanner> plan = engine.generateProductionPlan("Oyster", 500.0, 200);
        assertThat(plan).isNotEmpty();
        assertThat(plan.get(0).weekNumber()).isPositive();
    }

    @Test
    void generateProductionPlan_ForButton_ShouldReturnLongerPlan() {
        List<BusinessPlan.ProductionPlanner> plan = engine.generateProductionPlan("Button", 1000.0, 400);
        assertThat(plan).isNotEmpty();
    }

    @Test
    void generateProductionPlan_ShouldContainTasks() {
        List<BusinessPlan.ProductionPlanner> plan = engine.generateProductionPlan("Oyster", 500.0, 200);
        assertThat(plan.get(0).tasks()).isNotEmpty();
    }

    @Test
    void getMarketInsights_ForUnknownLocation_ShouldReturnGeneric() {
        List<String> insights = engine.getMarketInsights("Oyster", "Unknown");
        assertThat(insights).isNotEmpty();
    }

    @Test
    void calculateROI_WithZeroInvestment_ShouldHandleGracefully() {
        double roi = engine.calculateROI("Oyster", 0.0, 500.0, "Punjab");
        assertThat(roi).isNotNaN();
    }
}
