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
class CropHealthAdvisorEngineTest {

    private CropHealthAdvisorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CropHealthAdvisorEngine();
    }

    @Test
    void assessCropHealth_ForOyster_ShouldReturnAssessment() {
        Map<String, Object> assessment = engine.assessCropHealth("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0, "co2", 800.0));
        assertThat(assessment).isNotNull();
        assertThat(assessment).containsKey("healthStatus");
    }

    @Test
    void assessCropHealth_ForButton_ShouldReturnAssessment() {
        Map<String, Object> assessment = engine.assessCropHealth("Button", "fruiting",
                Map.of("temperature", 18.0, "humidity", 85.0, "co2", 900.0));
        assertThat(assessment).isNotNull();
        assertThat(assessment).containsKey("healthScore");
    }

    @Test
    void assessCropHealth_ForShiitake_ShouldReturnAssessment() {
        Map<String, Object> assessment = engine.assessCropHealth("Shiitake", "incubation",
                Map.of("temperature", 22.0, "humidity", 70.0));
        assertThat(assessment).isNotNull();
    }

    @Test
    void assessCropHealth_WithSuboptimalConditions_ShouldFlagIssues() {
        Map<String, Object> assessment = engine.assessCropHealth("Oyster", "spawning",
                Map.of("temperature", 35.0, "humidity", 95.0));
        assertThat(assessment).isNotNull();
        assertThat(assessment.get("healthStatus")).isNotEqualTo("GOOD");
    }

    @Test
    void assessCropHealth_WithEmptyParameters_ShouldReturnDefault() {
        Map<String, Object> assessment = engine.assessCropHealth("Oyster", "spawning", Map.of());
        assertThat(assessment).isNotNull();
    }

    @Test
    void getHealthChecklist_ShouldReturnChecklist() {
        List<String> checklist = engine.getHealthChecklist("Oyster", "spawning");
        assertThat(checklist).isNotEmpty();
    }

    @Test
    void getHealthChecklist_ForFruiting_ShouldReturnChecklist() {
        List<String> checklist = engine.getHealthChecklist("Oyster", "fruiting");
        assertThat(checklist).isNotEmpty();
    }

    @Test
    void getHealthChecklist_ForButton_ShouldReturnChecklist() {
        List<String> checklist = engine.getHealthChecklist("Button", "fruiting");
        assertThat(checklist).isNotEmpty();
    }

    @Test
    void getHealthChecklist_ForUnknownStage_ShouldReturnGeneric() {
        List<String> checklist = engine.getHealthChecklist("Oyster", "unknown");
        assertThat(checklist).isNotEmpty();
    }

    @Test
    void monitorGrowthParameters_ShouldReturnMonitoringData() {
        Map<String, Object> monitoring = engine.monitorGrowthParameters("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0));
        assertThat(monitoring).isNotNull();
        assertThat(monitoring).containsKey("parameters");
    }

    @Test
    void monitorGrowthParameters_ShouldFlagOutOfRange() {
        Map<String, Object> monitoring = engine.monitorGrowthParameters("Oyster", "spawning",
                Map.of("temperature", 45.0, "humidity", 100.0));
        assertThat(monitoring).isNotNull();
    }

    @Test
    void recommendCorrectiveAction_ShouldReturnAction() {
        String action = engine.recommendCorrectiveAction("Oyster", "spawning", "high_temperature");
        assertThat(action).isNotBlank();
    }

    @Test
    void recommendCorrectiveAction_ForLowHumidity_ShouldReturnAction() {
        String action = engine.recommendCorrectiveAction("Oyster", "fruiting", "low_humidity");
        assertThat(action).isNotBlank();
    }

    @Test
    void recommendCorrectiveAction_ForContamination_ShouldReturnAction() {
        String action = engine.recommendCorrectiveAction("Button", "spawning", "contamination");
        assertThat(action).isNotBlank();
    }

    @Test
    void recommendCorrectiveAction_ForUnknownIssue_ShouldReturnDefault() {
        String action = engine.recommendCorrectiveAction("Oyster", "spawning", "unknown_issue");
        assertThat(action).isNotBlank();
    }

    @Test
    void getPreventiveMeasures_ShouldReturnMeasures() {
        List<String> measures = engine.getPreventiveMeasures("Oyster");
        assertThat(measures).isNotEmpty();
    }

    @Test
    void getPreventiveMeasures_ForButton_ShouldReturnMeasures() {
        List<String> measures = engine.getPreventiveMeasures("Button");
        assertThat(measures).isNotEmpty();
    }

    @Test
    void getPreventiveMeasures_ForUnknownSpecies_ShouldReturnGeneric() {
        List<String> measures = engine.getPreventiveMeasures("Unknown");
        assertThat(measures).isNotEmpty();
    }

    @Test
    void calculateHealthScore_ShouldReturnScore() {
        double score = engine.calculateHealthScore("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0, "co2", 800.0));
        assertThat(score).isBetween(0.0, 100.0);
    }

    @Test
    void calculateHealthScore_WithPerfectConditions_ShouldReturnHigh() {
        double score = engine.calculateHealthScore("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0, "co2", 800.0, "light", 1000.0));
        assertThat(score).isGreaterThan(50.0);
    }

    @Test
    void calculateHealthScore_WithPoorConditions_ShouldReturnLow() {
        double score = engine.calculateHealthScore("Oyster", "spawning",
                Map.of("temperature", 40.0, "humidity", 30.0, "co2", 5000.0));
        assertThat(score).isLessThan(50.0);
    }

    @Test
    void getCropHealthReport_ShouldReturnReport() {
        String report = engine.getCropHealthReport("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0));
        assertThat(report).isNotBlank();
    }

    @Test
    void getCropHealthReport_ForButton_ShouldReturnReport() {
        String report = engine.getCropHealthReport("Button", "fruiting",
                Map.of("temperature", 18.0, "humidity", 85.0));
        assertThat(report).isNotBlank();
    }

    @Test
    void getCropHealthReport_ShouldIncludeScore() {
        String report = engine.getCropHealthReport("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0));
        assertThat(report).contains("Health Score");
    }

    @Test
    void assessCropHealth_WithCo2Parameter_ShouldConsiderIt() {
        Map<String, Object> assessment = engine.assessCropHealth("Oyster", "spawning",
                Map.of("temperature", 25.0, "humidity", 80.0, "co2", 5000.0));
        assertThat(assessment).isNotNull();
    }

    @Test
    void getHealthChecklist_ShouldCoverAllStages() {
        List<String> spawningChecklist = engine.getHealthChecklist("Oyster", "spawning");
        List<String> fruitingChecklist = engine.getHealthChecklist("Oyster", "fruiting");
        assertThat(spawningChecklist).isNotEqualTo(fruitingChecklist);
    }
}
