package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.CultivationStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CultivationAdvisorEngineTest {

    private CultivationAdvisorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CultivationAdvisorEngine();
    }

    @Test
    void getCultivationOverview_ForOyster_ShouldReturnOverview() {
        Map<String, Object> overview = engine.getCultivationOverview("Oyster");
        assertThat(overview).isNotNull();
        assertThat(overview).containsKey("species");
        assertThat(overview.get("species")).toString().toLowerCase().contains("oyster");
    }

    @Test
    void getCultivationOverview_ForButton_ShouldReturnOverview() {
        Map<String, Object> overview = engine.getCultivationOverview("Button");
        assertThat(overview).isNotNull();
        assertThat(overview).containsKey("totalDurationDays");
    }

    @Test
    void getCultivationOverview_ForShiitake_ShouldReturnOverview() {
        Map<String, Object> overview = engine.getCultivationOverview("Shiitake");
        assertThat(overview).isNotNull();
        assertThat(overview).containsKey("stages");
    }

    @Test
    void getCultivationOverview_ForMilky_ShouldReturnOverview() {
        Map<String, Object> overview = engine.getCultivationOverview("Milky");
        assertThat(overview).isNotNull();
        assertThat(overview).containsKey("species");
    }

    @Test
    void getCultivationOverview_ForPaddyStraw_ShouldReturnOverview() {
        Map<String, Object> overview = engine.getCultivationOverview("Paddy Straw");
        assertThat(overview).isNotNull();
        assertThat(overview).containsKey("totalDurationDays");
    }

    @Test
    void getCultivationOverview_ForUnknown_ShouldReturnDefault() {
        Map<String, Object> overview = engine.getCultivationOverview("Unknown");
        assertThat(overview).isNotNull();
        assertThat(overview.get("species")).isEqualTo("Unknown");
    }

    @Test
    void getStageGuidance_ShouldReturnStages() {
        List<CultivationStage> stages = engine.getStageGuidance("Oyster");
        assertThat(stages).isNotEmpty();
        assertThat(stages.get(0).stageName()).isNotBlank();
    }

    @Test
    void getStageGuidance_ForButton_ShouldReturnStages() {
        List<CultivationStage> stages = engine.getStageGuidance("Button");
        assertThat(stages).isNotEmpty();
        assertThat(stages.get(0).stageOrder()).isPositive();
    }

    @Test
    void getStageGuidance_ForUnknownSpecies_ShouldReturnEmpty() {
        List<CultivationStage> stages = engine.getStageGuidance("Unknown");
        assertThat(stages).isEmpty();
    }

    @Test
    void getCurrentStageAdvice_ShouldReturnAdvice() {
        String advice = engine.getCurrentStageAdvice("Oyster", "spawning");
        assertThat(advice).isNotBlank();
        assertThat(advice).containsIgnoringCase("spawning");
    }

    @Test
    void getCurrentStageAdvice_ForIncubation_ShouldReturnAdvice() {
        String advice = engine.getCurrentStageAdvice("Oyster", "incubation");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getCurrentStageAdvice_ForFruiting_ShouldReturnAdvice() {
        String advice = engine.getCurrentStageAdvice("Button", "fruiting");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getCurrentStageAdvice_ForHarvest_ShouldReturnAdvice() {
        String advice = engine.getCurrentStageAdvice("Shiitake", "harvest");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getCurrentStageAdvice_ForUnknownStage_ShouldReturnDefault() {
        String advice = engine.getCurrentStageAdvice("Oyster", "unknown");
        assertThat(advice).isNotBlank();
    }

    @Test
    void answerCultivationQuestion_ShouldReturnAnswer() {
        String answer = engine.answerCultivationQuestion("Oyster", "What temperature is best?");
        assertThat(answer).isNotBlank();
    }

    @Test
    void answerCultivationQuestion_ForButton_ShouldReturnAnswer() {
        String answer = engine.answerCultivationQuestion("Button", "How to prepare compost?");
        assertThat(answer).isNotBlank();
    }

    @Test
    void answerCultivationQuestion_WithEmptyQuestion_ShouldReturnDefault() {
        String answer = engine.answerCultivationQuestion("Oyster", "");
        assertThat(answer).isNotBlank();
    }

    @Test
    void getTroubleshootingTips_ShouldReturnTips() {
        List<String> tips = engine.getTroubleshootingTips("Oyster");
        assertThat(tips).isNotEmpty();
    }

    @Test
    void getTroubleshootingTips_ForButton_ShouldReturnTips() {
        List<String> tips = engine.getTroubleshootingTips("Button");
        assertThat(tips).isNotEmpty();
    }

    @Test
    void getTroubleshootingTips_ForUnknownSpecies_ShouldReturnGeneric() {
        List<String> tips = engine.getTroubleshootingTips("Unknown");
        assertThat(tips).isNotEmpty();
    }

    @Test
    void getOptimalConditions_ShouldReturnConditions() {
        Map<String, Object> conditions = engine.getOptimalConditions("Oyster");
        assertThat(conditions).isNotNull();
        assertThat(conditions).containsKey("temperature");
    }

    @Test
    void getOptimalConditions_ForButton_ShouldReturnConditions() {
        Map<String, Object> conditions = engine.getOptimalConditions("Button");
        assertThat(conditions).isNotNull();
        assertThat(conditions).containsKey("humidity");
    }

    @Test
    void getOptimalConditions_ForShiitake_ShouldReturnConditions() {
        Map<String, Object> conditions = engine.getOptimalConditions("Shiitake");
        assertThat(conditions).isNotNull();
        assertThat(conditions).containsKey("co2");
    }

    @Test
    void getWaterManagementAdvice_ShouldReturnAdvice() {
        String advice = engine.getWaterManagementAdvice("Oyster", "spawning");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getWaterManagementAdvice_ForFruiting_ShouldReturnAdvice() {
        String advice = engine.getWaterManagementAdvice("Oyster", "fruiting");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getWaterManagementAdvice_ForHarvest_ShouldReturnAdvice() {
        String advice = engine.getWaterManagementAdvice("Button", "harvest");
        assertThat(advice).isNotBlank();
    }

    @Test
    void getWaterManagementAdvice_ForUnknownStage_ShouldReturnDefault() {
        String advice = engine.getWaterManagementAdvice("Oyster", "unknown");
        assertThat(advice).isNotBlank();
    }
}
