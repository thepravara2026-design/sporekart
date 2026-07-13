package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TrustFactorTest {

    @ParameterizedTest
    @EnumSource(TrustFactor.class)
    void shouldContainAllExpectedValues(TrustFactor factor) {
        assertNotNull(factor);
        assertNotNull(factor.name());
    }

    @ParameterizedTest
    @EnumSource(value = TrustFactor.class, names = { "PROVIDER_RELIABILITY", "KNOWLEDGE_QUALITY", "SEMANTIC_CONFIDENCE", "PROMPT_VALIDATION", "HISTORICAL_ACCURACY", "POLICY_COMPLIANCE", "WORKFLOW_SUCCESS", "CONTEXT_COMPLETENESS", "OUTPUT_VALIDATION" })
    void shouldHaveNineValues(TrustFactor factor) {
        assertTrue(factor.ordinal() >= 0);
    }

    @Test
    void providerReliabilityShouldBeFirst() {
        assertEquals(0, TrustFactor.PROVIDER_RELIABILITY.ordinal());
    }

    @Test
    void outputValidationShouldBeLast() {
        assertEquals(8, TrustFactor.OUTPUT_VALIDATION.ordinal());
    }

    @Test
    void valueOfShouldWorkForAll() {
        assertEquals(TrustFactor.PROVIDER_RELIABILITY, TrustFactor.valueOf("PROVIDER_RELIABILITY"));
        assertEquals(TrustFactor.KNOWLEDGE_QUALITY, TrustFactor.valueOf("KNOWLEDGE_QUALITY"));
        assertEquals(TrustFactor.SEMANTIC_CONFIDENCE, TrustFactor.valueOf("SEMANTIC_CONFIDENCE"));
        assertEquals(TrustFactor.PROMPT_VALIDATION, TrustFactor.valueOf("PROMPT_VALIDATION"));
        assertEquals(TrustFactor.HISTORICAL_ACCURACY, TrustFactor.valueOf("HISTORICAL_ACCURACY"));
        assertEquals(TrustFactor.POLICY_COMPLIANCE, TrustFactor.valueOf("POLICY_COMPLIANCE"));
        assertEquals(TrustFactor.WORKFLOW_SUCCESS, TrustFactor.valueOf("WORKFLOW_SUCCESS"));
        assertEquals(TrustFactor.CONTEXT_COMPLETENESS, TrustFactor.valueOf("CONTEXT_COMPLETENESS"));
        assertEquals(TrustFactor.OUTPUT_VALIDATION, TrustFactor.valueOf("OUTPUT_VALIDATION"));
    }
}
