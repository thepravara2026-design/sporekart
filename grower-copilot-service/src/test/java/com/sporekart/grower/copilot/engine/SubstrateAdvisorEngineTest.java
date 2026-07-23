package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.SubstrateRecommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SubstrateAdvisorEngineTest {

    private SubstrateAdvisorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new SubstrateAdvisorEngine();
    }

    @Test
    void recommendSubstrate_ForOyster_ShouldReturnRecommendations() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Oyster");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSubstrate_ForButton_ShouldReturnRecommendations() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Button");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSubstrate_ForShiitake_ShouldReturnRecommendations() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Shiitake");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSubstrate_ForMilky_ShouldReturnRecommendations() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Milky");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSubstrate_ForPaddyStraw_ShouldReturnRecommendations() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Paddy Straw");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSubstrate_ForUnknown_ShouldReturnEmpty() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Unknown");
        assertThat(results).isEmpty();
    }

    @Test
    void getSubstrateById_ShouldReturnSubstrate() {
        SubstrateRecommendation substrate = engine.getSubstrateById("SUB-001");
        assertThat(substrate).isNotNull();
        assertThat(substrate.substrateId()).isEqualTo("SUB-001");
    }

    @Test
    void getSubstrateById_WithInvalidId_ShouldReturnNull() {
        SubstrateRecommendation substrate = engine.getSubstrateById("INVALID");
        assertThat(substrate).isNull();
    }

    @Test
    void getAllSubstrates_ShouldReturnAll() {
        List<SubstrateRecommendation> all = engine.getAllSubstrates();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isGreaterThan(3);
    }

    @Test
    void getSubstratePreparationGuide_ShouldReturnGuide() {
        String guide = engine.getSubstratePreparationGuide("SUB-001");
        assertThat(guide).isNotBlank();
    }

    @Test
    void getSubstratePreparationGuide_ForInvalidId_ShouldReturnDefault() {
        String guide = engine.getSubstratePreparationGuide("INVALID");
        assertThat(guide).isNotBlank();
    }

    @Test
    void getSterilizationMethod_ShouldReturnMethod() {
        String method = engine.getSterilizationMethod("SUB-001");
        assertThat(method).isNotBlank();
    }

    @Test
    void getSterilizationMethod_ForInvalidId_ShouldReturnDefault() {
        String method = engine.getSterilizationMethod("INVALID");
        assertThat(method).isNotBlank();
    }

    @Test
    void compareSubstrates_ShouldReturnComparison() {
        String comparison = engine.compareSubstrates("SUB-001", "SUB-002");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void compareSubstrates_WithSameIds_ShouldReturnComparison() {
        String comparison = engine.compareSubstrates("SUB-001", "SUB-001");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void compareSubstrates_WithInvalidId_ShouldReturnDefault() {
        String comparison = engine.compareSubstrates("INVALID", "SUB-001");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void getAllSubstrates_ShouldContainWheatStraw() {
        List<SubstrateRecommendation> all = engine.getAllSubstrates();
        List<String> names = all.stream().map(SubstrateRecommendation::name).toList();
        assertThat(names).anyMatch(n -> n.toLowerCase().contains("straw"));
    }

    @Test
    void recommendSubstrate_ShouldReturnWithSuitableSpecies() {
        List<SubstrateRecommendation> results = engine.recommendSubstrate("Oyster");
        SubstrateRecommendation first = results.get(0);
        assertThat(first.suitableSpecies()).anyMatch(s -> s.equalsIgnoreCase("Oyster"));
    }

    @Test
    void getSterilizationMethod_ShouldContainTemperature() {
        String method = engine.getSterilizationMethod("SUB-001");
        assertThat(method).contains("°C");
    }

    @Test
    void getSubstratePreparationGuide_ShouldContainSteps() {
        String guide = engine.getSubstratePreparationGuide("SUB-001");
        assertThat(guide).contains("Step");
    }
}
