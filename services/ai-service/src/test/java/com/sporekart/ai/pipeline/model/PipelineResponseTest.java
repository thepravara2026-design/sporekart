package com.sporekart.ai.pipeline.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PipelineResponseTest {
    @Test
    void shouldCreateSuccessResponse() {
        var response = PipelineResponse.success(
                "resp-1", "req-1", "OPENAI", "gpt-4",
                "Hello!", TokenUsage.of(10, 20), Duration.ofMillis(100));
        assertTrue(response.success());
        assertEquals("OPENAI", response.provider());
    }

    @Test
    void shouldCreateFailureResponse() {
        var response = PipelineResponse.failure("req-1", "Something went wrong", "OPENAI");
        assertFalse(response.success());
        assertEquals(1, response.errors().size());
        assertEquals("Something went wrong", response.errors().get(0));
    }

    @Test
    void shouldUseBuilderDefaults() {
        var response = PipelineResponse.builder()
                .responseId("r").requestId("req").build();
        assertNotNull(response.completedAt());
        assertEquals(FinishReason.ERROR, response.finishReason());
    }

    @Test
    void shouldBuildWithAllFields() {
        var response = PipelineResponse.builder()
                .responseId("resp-1")
                .requestId("req-1")
                .provider("GEMINI")
                .model("gemini-pro")
                .generatedOutput("Output")
                .structuredOutput("{}")
                .tokenUsage(TokenUsage.of(10, 20))
                .latency(Duration.ofMillis(200))
                .finishReason(FinishReason.STOP)
                .cost(0.001)
                .success(true)
                .addWarning("slow")
                .addMetadata("key", "value")
                .build();
        assertEquals("GEMINI", response.provider());
        assertEquals(1, response.warnings().size());
        assertEquals("slow", response.warnings().get(0));
        assertEquals("value", response.metadata().get("key"));
    }

    @Test
    void shouldHandleNulls() {
        var response = new PipelineResponse("r", "req", null, null, null, null,
                null, null, null, 0, null, null, null, null, true, null);
        assertNotNull(response.warnings());
        assertNotNull(response.errors());
        assertNotNull(response.metadata());
        assertNotNull(response.completedAt());
    }
}
