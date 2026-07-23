package com.sporekart.trainer.copilot.engine;

import com.sporekart.trainer.copilot.domain.CultivationStep;
import com.sporekart.trainer.copilot.domain.PracticalGuide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PracticalCultivationEngineTest {

    private PracticalCultivationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new PracticalCultivationEngine();
    }

    @Test
    void getCultivationGuideForSpawnPreparation() {
        PracticalGuide guide = engine.getCultivationGuide("SPAWN_PREPARATION");

        assertNotNull(guide);
        assertEquals("SPAWN_PREPARATION", guide.category());
        assertNotNull(guide.steps());
        assertFalse(guide.steps().isEmpty());
    }

    @Test
    void getCultivationGuideForSubstratePreparation() {
        PracticalGuide guide = engine.getCultivationGuide("SUBSTRATE_PREPARATION");

        assertNotNull(guide);
        assertEquals("SUBSTRATE_PREPARATION", guide.category());
        assertNotNull(guide.requiredMaterials());
    }

    @Test
    void getCultivationGuideForInoculation() {
        PracticalGuide guide = engine.getCultivationGuide("INOCULATION");

        assertNotNull(guide);
        assertEquals("INOCULATION", guide.category());
        assertNotNull(guide.safetyPrecautions());
    }

    @Test
    void getCultivationGuideForFruiting() {
        PracticalGuide guide = engine.getCultivationGuide("FRUITING");

        assertNotNull(guide);
        assertEquals("FRUITING", guide.category());
        assertNotNull(guide.commonMistakes());
    }

    @Test
    void getCultivationGuideForHarvesting() {
        PracticalGuide guide = engine.getCultivationGuide("HARVESTING");

        assertNotNull(guide);
        assertEquals("HARVESTING", guide.category());
    }

    @Test
    void getCultivationStepsReturnsOrderedSteps() {
        List<CultivationStep> steps = engine.getCultivationSteps("SPAWN_PREPARATION");

        assertNotNull(steps);
        assertFalse(steps.isEmpty());
        for (int i = 0; i < steps.size() - 1; i++) {
            assertTrue(steps.get(i).stepOrder() <= steps.get(i + 1).stepOrder());
        }
    }

    @Test
    void identifyDiseaseReturnsDiagnosis() {
        Map<String, Object> diagnosis = engine.identifyDisease("Green mold on substrate");

        assertNotNull(diagnosis.get("diseaseName"));
        assertNotNull(diagnosis.get("cause"));
        assertNotNull(diagnosis.get("treatment"));
        assertNotNull(diagnosis.get("prevention"));
    }

    @Test
    void identifyDiseaseWithUnknownSymptoms() {
        Map<String, Object> diagnosis = engine.identifyDisease("Unknown discoloration");

        assertNotNull(diagnosis.get("diseaseName"));
    }

    @Test
    void getBestPracticesReturnsGuidelines() {
        List<String> practices = engine.getBestPractices("STERILE_TECHNIQUE");

        assertNotNull(practices);
        assertFalse(practices.isEmpty());
    }

    @Test
    void getTroubleshootingGuideReturnsSolutions() {
        Map<String, Object> guide = engine.getTroubleshootingGuide("CONTAMINATION");

        assertNotNull(guide.get("issue"));
        assertNotNull(guide.get("symptoms"));
        assertNotNull(guide.get("solutions"));
    }

    @Test
    void getAllGuidesReturnsAllCategories() {
        Map<String, PracticalGuide> guides = engine.getAllGuides();

        assertNotNull(guides);
        assertTrue(guides.size() >= 5);
        assertTrue(guides.containsKey("SPAWN_PREPARATION"));
        assertTrue(guides.containsKey("SUBSTRATE_PREPARATION"));
        assertTrue(guides.containsKey("INOCULATION"));
        assertTrue(guides.containsKey("FRUITING"));
        assertTrue(guides.containsKey("HARVESTING"));
    }
}
