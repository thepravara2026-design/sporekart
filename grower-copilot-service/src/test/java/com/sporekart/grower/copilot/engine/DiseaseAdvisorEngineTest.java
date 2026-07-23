package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.DiseaseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DiseaseAdvisorEngineTest {

    private DiseaseAdvisorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new DiseaseAdvisorEngine();
    }

    @Test
    void diagnoseBySymptoms_WithGreenMoldSymptoms_ShouldReturnGreenMold() {
        List<String> symptoms = List.of("Green sporulation on substrate", "Foul odor");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Green Mold");
    }

    @Test
    void diagnoseBySymptoms_WithBacterialBlotchSymptoms_ShouldReturnBlotch() {
        List<String> symptoms = List.of("Brown spots on caps", "Slimy lesions");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Blotch");
    }

    @Test
    void diagnoseBySymptoms_WithCobwebSymptoms_ShouldReturnCobweb() {
        List<String> symptoms = List.of("Cobweb-like mycelium on casing", "Rapid spread across surface");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Cobweb");
    }

    @Test
    void diagnoseBySymptoms_WithDryBubbleSymptoms_ShouldReturnDryBubble() {
        List<String> symptoms = List.of("Deformed mushrooms", "Brown spots on caps");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Dry Bubble");
    }

    @Test
    void diagnoseBySymptoms_WithWetBubbleSymptoms_ShouldReturnWetBubble() {
        List<String> symptoms = List.of("Amber droplets on mushrooms", "Severe mushroom deformation");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Wet Bubble");
    }

    @Test
    void diagnoseBySymptoms_WithMiteSymptoms_ShouldReturnMite() {
        List<String> symptoms = List.of("Tiny insects visible on gills and stipe", "Brown discoloration of gills");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).diseaseName()).contains("Mite");
    }

    @Test
    void diagnoseBySymptoms_WithEmptySymptoms_ShouldReturnEmpty() {
        List<String> symptoms = List.of();
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isEmpty();
    }

    @Test
    void diagnoseBySymptoms_WithUnknownSymptoms_ShouldReturnEmpty() {
        List<String> symptoms = List.of("Purple spots", "Glowing mycelium");
        List<DiseaseInfo> results = engine.diagnoseBySymptoms(symptoms);
        assertThat(results).isEmpty();
    }

    @Test
    void diagnoseBySymptoms_WithMushroomTypeAndStage_ShouldReturnDiseaseResponse() {
        List<String> symptoms = List.of("Green sporulation on substrate");
        DiseaseAdvisorEngine.DiseaseResponse response = engine.diagnoseBySymptoms(symptoms, "oyster", "fruiting");
        assertThat(response).isNotNull();
        assertThat(response.diagnosisId()).isNotBlank();
        assertThat(response.probableDiseases()).isNotEmpty();
        assertThat(response.primaryDiagnosis()).isNotBlank();
    }

    @Test
    void diagnoseBySymptoms_WithUnknownSymptomsAndType_ShouldReturnNoMatch() {
        List<String> symptoms = List.of("Purple discoloration");
        DiseaseAdvisorEngine.DiseaseResponse response = engine.diagnoseBySymptoms(symptoms, "oyster", "fruiting");
        assertThat(response).isNotNull();
        assertThat(response.primaryDiagnosis()).contains("No matching disease found");
    }

    @Test
    void diagnoseBySymptoms_WithEscalation_ShouldFlagEscalation() {
        List<String> symptoms = List.of("Green sporulation on substrate", "Foul odor", "Rapid spread across casing");
        DiseaseAdvisorEngine.DiseaseResponse response = engine.diagnoseBySymptoms(symptoms, "oyster", "spawn run");
        assertThat(response.requiresEscalation()).isTrue();
    }

    @Test
    void getDiseaseById_ShouldReturnDisease() {
        DiseaseInfo disease = engine.getDiseaseById("D001");
        assertThat(disease).isNotNull();
        assertThat(disease.diseaseName()).isEqualTo("Green Mold");
    }

    @Test
    void getDiseaseById_ForBacterialBlotch_ShouldReturnDisease() {
        DiseaseInfo disease = engine.getDiseaseById("D002");
        assertThat(disease).isNotNull();
        assertThat(disease.diseaseName()).isEqualTo("Bacterial Blotch");
    }

    @Test
    void getDiseaseById_WithInvalidId_ShouldReturnNull() {
        DiseaseInfo disease = engine.getDiseaseById("INVALID");
        assertThat(disease).isNull();
    }

    @Test
    void getAllDiseases_ShouldReturnAll() {
        List<DiseaseInfo> diseases = engine.getAllDiseases();
        assertThat(diseases).isNotEmpty();
        assertThat(diseases.size()).isGreaterThanOrEqualTo(18);
    }

    @Test
    void getDiseasesByCategory_ShouldReturnMatching() {
        List<DiseaseInfo> fungal = engine.getDiseasesByCategory("Fungal");
        assertThat(fungal).isNotEmpty();
        assertThat(fungal).allMatch(d -> d.category().equalsIgnoreCase("Fungal"));
    }

    @Test
    void getDiseasesByCategory_Bacterial_ShouldReturnMatching() {
        List<DiseaseInfo> bacterial = engine.getDiseasesByCategory("Bacterial");
        assertThat(bacterial).isNotEmpty();
    }

    @Test
    void getDiseasesByCategory_Pest_ShouldReturnMatching() {
        List<DiseaseInfo> pest = engine.getDiseasesByCategory("Pest");
        assertThat(pest).isNotEmpty();
    }

    @Test
    void getDiseasesBySeverity_ShouldReturnMatching() {
        List<DiseaseInfo> high = engine.getDiseasesBySeverity("HIGH");
        assertThat(high).isNotEmpty();
        assertThat(high).allMatch(d -> d.severity().equalsIgnoreCase("HIGH"));
    }

    @Test
    void getDiseasesBySeverity_CRITICAL_ShouldReturnMatching() {
        List<DiseaseInfo> critical = engine.getDiseasesBySeverity("CRITICAL");
        assertThat(critical).isNotEmpty();
    }

    @Test
    void getDiseasesBySeverity_LOW_ShouldReturnMatching() {
        List<DiseaseInfo> low = engine.getDiseasesBySeverity("LOW");
        assertThat(low).isNotEmpty();
    }

    @Test
    void getPreventionTips_ShouldReturnTips() {
        List<String> tips = engine.getPreventionTips("D001");
        assertThat(tips).isNotEmpty();
    }

    @Test
    void getPreventionTips_ForInvalidId_ShouldReturnEmpty() {
        List<String> tips = engine.getPreventionTips("INVALID");
        assertThat(tips).isEmpty();
    }

    @Test
    void getTreatmentPlan_ShouldReturnPlan() {
        String plan = engine.getTreatmentPlan("D001");
        assertThat(plan).isNotBlank();
        assertThat(plan).contains("Remove infected substrate");
    }

    @Test
    void getTreatmentPlan_ForInvalidId_ShouldReturnDefault() {
        String plan = engine.getTreatmentPlan("INVALID");
        assertThat(plan).contains("No treatment plan available");
    }

    @Test
    void getSeasonalDiseaseRisk_ShouldReturnRisks() {
        List<DiseaseInfo> risks = engine.getSeasonalDiseaseRisk(7, "Punjab");
        assertThat(risks).isNotEmpty();
        assertThat(risks.get(0).probabilityScore()).isPositive();
    }

    @Test
    void getSeasonalDiseaseRisk_WinterMonth_ShouldReturnLowerRisks() {
        List<DiseaseInfo> risks = engine.getSeasonalDiseaseRisk(1, "Himachal Pradesh");
        assertThat(risks).isNotEmpty();
    }

    @Test
    void escalateCase_HighSeverity_ShouldReturnTrue() {
        DiseaseInfo disease = engine.getDiseaseById("D001");
        boolean escalate = engine.escalateCase(disease);
        assertThat(escalate).isTrue();
    }

    @Test
    void escalateCase_LowSeverity_ShouldReturnFalse() {
        DiseaseInfo disease = engine.getDiseaseById("D006");
        boolean escalate = engine.escalateCase(disease);
        assertThat(escalate).isFalse();
    }

    @Test
    void getCommonDiseases_ForOyster_ShouldReturnRelevant() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Oyster");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getCommonDiseases_ForButton_ShouldReturnRelevant() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Button");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getCommonDiseases_ForShiitake_ShouldReturnRelevant() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Shiitake");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getCommonDiseases_ForMilky_ShouldReturnRelevant() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Milky");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getCommonDiseases_ForPaddyStraw_ShouldReturnRelevant() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Paddy Straw");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getCommonDiseases_ForUnknown_ShouldReturnGeneric() {
        List<DiseaseInfo> diseases = engine.getCommonDiseases("Unknown");
        assertThat(diseases).isNotEmpty();
    }

    @Test
    void getDiseasesByCategory_WithUnknownCategory_ShouldReturnEmpty() {
        List<DiseaseInfo> result = engine.getDiseasesByCategory("UnknownCategory");
        assertThat(result).isEmpty();
    }

    @Test
    void getDiseasesBySeverity_WithUnknownSeverity_ShouldReturnEmpty() {
        List<DiseaseInfo> result = engine.getDiseasesBySeverity("UNKNOWN");
        assertThat(result).isEmpty();
    }
}
