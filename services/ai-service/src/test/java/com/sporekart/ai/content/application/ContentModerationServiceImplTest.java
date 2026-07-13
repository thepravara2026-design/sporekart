package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentModerationRequest;
import com.sporekart.ai.content.domain.ModerationStatus;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentModerationServiceImplTest {

    private ContentModerationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentModerationServiceImpl();
    }

    @Test
    void shouldModerateApprovedContent() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Safe content here",
                ContentType.TEXT, true, true, true);

        var result = service.moderate(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertEquals(ModerationStatus.APPROVED, result.status());
        assertFalse(result.containsPii());
        assertFalse(result.containsProfanity());
        assertFalse(result.isToxic());
        assertTrue(result.confidenceScore() > 0);
        assertTrue(result.flags().isEmpty());
        assertFalse(result.humanReviewRequired());
        assertNull(result.reviewedBy());
        assertNull(result.reviewedAt());
    }

    @Test
    void shouldModerateWithPiiCheck() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Content with email@test.com",
                ContentType.TEXT, true, false, false);

        var result = service.moderate(request);

        assertNotNull(result);
        assertFalse(result.containsPii());
        assertFalse(result.containsProfanity());
    }

    @Test
    void shouldModerateWithProfanityCheck() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Clean content",
                ContentType.TEXT, false, true, false);

        var result = service.moderate(request);

        assertNotNull(result);
        assertFalse(result.containsProfanity());
    }

    @Test
    void shouldModerateWithToxicityCheck() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Friendly content",
                ContentType.TEXT, false, false, true);

        var result = service.moderate(request);

        assertNotNull(result);
        assertFalse(result.isToxic());
    }

    @Test
    void shouldReviewAndApprove() {
        var moderationId = UUID.randomUUID();
        var reviewerId = UUID.randomUUID();

        var result = service.review(moderationId, reviewerId, ModerationStatus.APPROVED);

        assertNotNull(result);
        assertEquals(moderationId, result.id());
        assertEquals(ModerationStatus.APPROVED, result.status());
        assertFalse(result.humanReviewRequired());
        assertEquals(reviewerId, result.reviewedBy());
        assertNotNull(result.reviewedAt());
    }

    @Test
    void shouldReviewAndFlag() {
        var moderationId = UUID.randomUUID();
        var reviewerId = UUID.randomUUID();

        var result = service.review(moderationId, reviewerId, ModerationStatus.FLAGGED_FOR_REVIEW);

        assertNotNull(result);
        assertEquals(ModerationStatus.FLAGGED_FOR_REVIEW, result.status());
        assertTrue(result.humanReviewRequired());
        assertEquals(reviewerId, result.reviewedBy());
    }

    @Test
    void shouldGetModerationReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getModeration(id);

        assertTrue(result.isEmpty());
    }
}
