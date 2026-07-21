package com.sporekart.ai.pipeline.normalization;

import com.sporekart.ai.pipeline.model.FinishReason;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import com.sporekart.ai.pipeline.model.TokenUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseNormalizerTest {
    private ResponseNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new ResponseNormalizer();
    }

    @Test
    void shouldNormalizeSuccessResponse() {
        var response = PipelineResponse.success("r", "req", "OPENAI", "gpt-4",
                "  Hello!  ", TokenUsage.of(10, 20), Duration.ofMillis(100));
        var normalized = normalizer.normalize(response);
        assertEquals("Hello!", normalized.generatedText());
        assertTrue(normalized.success());
    }

    @Test
    void shouldReturnEmptyForNullResponse() {
        var normalized = normalizer.normalize(null);
        assertEquals("", normalized.generatedText());
        assertFalse(normalized.success());
    }

    @Test
    void shouldNormalizeCost() {
        var response = new PipelineResponse("r", "req", "P", "M", "O", null,
                TokenUsage.EMPTY, Duration.ZERO, FinishReason.STOP,
                0.00123456, List.of(), List.of(), null, null, true, null);
        var normalized = normalizer.normalize(response);
        assertEquals(0.001235, normalized.normalizedCost());
    }

    @Test
    void shouldStandardizeFinishReason() {
        assertEquals("stop", normalizer.standardizeFinishReason(FinishReason.STOP));
        assertEquals("length", normalizer.standardizeFinishReason(FinishReason.LENGTH));
        assertEquals("timeout", normalizer.standardizeFinishReason(FinishReason.TIMEOUT));
        assertEquals("unknown", normalizer.standardizeFinishReason(null));
    }

    @Test
    void shouldFilterEmptyWarnings() {
        var result = normalizer.normalizeWarnings(List.of("", "warning1", "  ", "warning2"));
        assertEquals(2, result.size());
    }

    @Test
    void shouldHandleNullWarnings() {
        var result = normalizer.normalizeWarnings(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNormalizeStructuredOutput() {
        assertEquals("{}", normalizer.normalizeStructuredOutput("  {}  "));
    }
}
