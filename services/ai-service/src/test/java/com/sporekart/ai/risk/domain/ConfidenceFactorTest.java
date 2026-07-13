package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ConfidenceFactorTest {

    @ParameterizedTest
    @EnumSource(ConfidenceFactor.class)
    void shouldContainAllExpectedValues(ConfidenceFactor factor) {
        assertNotNull(factor);
        assertNotNull(factor.name());
    }

    @ParameterizedTest
    @EnumSource(value = ConfidenceFactor.class, names = { "KNOWLEDGE_MATCH", "SEMANTIC_SIMILARITY", "PROMPT_QUALITY", "CONVERSATION_CONTEXT", "WORKFLOW_SUCCESS", "PROVIDER_METADATA" })
    void shouldHaveSixValues(ConfidenceFactor factor) {
        assertTrue(factor.ordinal() >= 0);
    }

    @Test
    void knowledgeMatchShouldBeFirst() {
        assertEquals(0, ConfidenceFactor.KNOWLEDGE_MATCH.ordinal());
    }

    @Test
    void providerMetadataShouldBeLast() {
        assertEquals(5, ConfidenceFactor.PROVIDER_METADATA.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(ConfidenceFactor.KNOWLEDGE_MATCH, ConfidenceFactor.valueOf("KNOWLEDGE_MATCH"));
        assertEquals(ConfidenceFactor.SEMANTIC_SIMILARITY, ConfidenceFactor.valueOf("SEMANTIC_SIMILARITY"));
        assertEquals(ConfidenceFactor.PROMPT_QUALITY, ConfidenceFactor.valueOf("PROMPT_QUALITY"));
        assertEquals(ConfidenceFactor.CONVERSATION_CONTEXT, ConfidenceFactor.valueOf("CONVERSATION_CONTEXT"));
        assertEquals(ConfidenceFactor.WORKFLOW_SUCCESS, ConfidenceFactor.valueOf("WORKFLOW_SUCCESS"));
        assertEquals(ConfidenceFactor.PROVIDER_METADATA, ConfidenceFactor.valueOf("PROVIDER_METADATA"));
    }
}
