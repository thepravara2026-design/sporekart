package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.BrandCheckRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandGovernanceEngineTest {

    @InjectMocks
    private BrandGovernanceEngine brandGovernanceEngine;

    @Test
    void testCheckContentCompliant() {
        var request = new BrandCheckRequest(
            "SporeKart provides quality mushroom cultivation expertise. Our sustainable organic products are innovative and reliable.",
            "professional", "article");
        var response = brandGovernanceEngine.checkContent(request);
        assertNotNull(response);
        assertTrue(response.consistencyScore() > 0);
        assertTrue(response.approved());
    }

    @Test
    void testCheckContentWithBannedTerms() {
        var request = new BrandCheckRequest(
            "This is cheap and low-quality knockoff product. SporeKart is better.",
            "professional", "article");
        var response = brandGovernanceEngine.checkContent(request);
        assertNotNull(response);
        assertTrue(response.consistencyScore() < 100);
        assertFalse(response.violations().isEmpty());
    }

    @Test
    void testCheckContentEmpty() {
        var request = new BrandCheckRequest("", "professional", "article");
        var response = brandGovernanceEngine.checkContent(request);
        assertNotNull(response);
        assertFalse(response.approved());
    }

    @Test
    void testPerformAudit() {
        var result = brandGovernanceEngine.performAudit("content-1",
            "SporeKart quality mushroom products", "professional");
        assertNotNull(result);
        assertNotNull(result.contentId());
        assertTrue(result.consistencyScore() >= 0);
        assertNotNull(result.violations());
    }

    @Test
    void testGetBrandGuidelines() {
        var guidelines = brandGovernanceEngine.getBrandGuidelines();
        assertFalse(guidelines.isEmpty());
        assertTrue(guidelines.get(0).contains("SporeKart"));
    }

    @Test
    void testEnforceConsistency() {
        var suggestions = brandGovernanceEngine.enforceConsistency("Great mushrooms!", "social");
        assertFalse(suggestions.isEmpty());
    }

    @Test
    void testEnforceConsistencyWithBrandMention() {
        var suggestions = brandGovernanceEngine.enforceConsistency("SporeKart has great mushrooms!", "email");
        assertFalse(suggestions.stream().anyMatch(s -> s.contains("SporeKart")));
    }
}
