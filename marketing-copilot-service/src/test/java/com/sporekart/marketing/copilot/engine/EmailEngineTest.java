package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailEngineTest {

    @InjectMocks
    private EmailEngine emailEngine;

    @Test
    void testGenerateEmail() {
        var request = new EmailRequest("Welcome to SporeKart", "welcome", "new_signups", "friendly",
            "Shop Now", null);
        var response = emailEngine.generateEmail(request);
        assertNotNull(response);
        assertNotNull(response.emailId());
        assertEquals("Welcome to SporeKart", response.subject());
        assertNotNull(response.preheader());
        assertNotNull(response.bodyHtml());
        assertNotNull(response.bodyText());
        assertFalse(response.recommendations().isEmpty());
    }

    @Test
    void testGenerateEmailPromotional() {
        var request = new EmailRequest("Summer Sale", "promotional", "active_buyers", "exciting",
            "Get 20% Off", "camp-1");
        var response = emailEngine.generateEmail(request);
        assertNotNull(response);
        assertEquals("Summer Sale", response.subject());
    }

    @Test
    void testCreateEmailCampaign() {
        var request = new EmailRequest("Test Campaign", "educational", "beginners", "friendly",
            "Read More", null);
        var campaign = emailEngine.createEmailCampaign(request);
        assertNotNull(campaign);
        assertEquals("Test Campaign", campaign.subject());
        assertEquals(com.sporekart.marketing.copilot.domain.EmailCampaign.EmailType.EDUCATIONAL, campaign.type());
    }

    @Test
    void testOptimizeSubjectLine() {
        var subjects = emailEngine.optimizeSubjectLine("Mushroom Growing", "beginner");
        assertEquals(5, subjects.size());
        assertTrue(subjects.get(0).contains("Mushroom Growing"));
    }

    @Test
    void testSegmentAudienceForPromotional() {
        var segments = emailEngine.segmentAudience("promotional");
        assertTrue(segments.contains("active_buyers"));
        assertTrue(segments.contains("cart_abandoners"));
    }

    @Test
    void testSegmentAudienceForEducational() {
        var segments = emailEngine.segmentAudience("educational");
        assertEquals(3, segments.size());
    }

    @Test
    void testSegmentAudienceForWelcome() {
        var segments = emailEngine.segmentAudience("welcome");
        assertTrue(segments.contains("new_signups"));
    }

    @Test
    void testSegmentAudienceDefault() {
        var segments = emailEngine.segmentAudience("unknown");
        assertEquals(1, segments.size());
        assertEquals("all_subscribers", segments.get(0));
    }
}
