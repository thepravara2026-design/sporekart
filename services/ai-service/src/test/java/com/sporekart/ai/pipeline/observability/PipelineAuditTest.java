package com.sporekart.ai.pipeline.observability;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PipelineAuditTest {
    private PipelineAudit audit;
    private PipelineContext context;

    @BeforeEach
    void setUp() {
        audit = new PipelineAudit();
        var request = PipelineRequest.builder()
                .requestId("req-1").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hi")).build();
        context = new PipelineContext(request);
    }

    @Test
    void shouldRecordRequestReceived() {
        var ref = audit.recordRequestReceived(context);
        assertNotNull(ref);
        assertEquals(1, audit.getAuditLog().size());
    }

    @Test
    void shouldRecordProviderSelection() {
        context.selectProvider("OPENAI", "gpt-4");
        var ref = audit.recordProviderSelection(context);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordExecutionStarted() {
        var ref = audit.recordExecutionStarted(context);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordExecutionCompleted() {
        context.setResponse(PipelineResponse.success("resp", "req", "P", "M", "O",
                null, Duration.ZERO));
        var ref = audit.recordExecutionCompleted(context);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordFailure() {
        context.fail("Something went wrong");
        var ref = audit.recordFailure(context);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordRetry() {
        var ref = audit.recordRetry(context, 1);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordTimeout() {
        var ref = audit.recordTimeout(context);
        assertNotNull(ref);
    }

    @Test
    void shouldRecordPipelineCompleted() {
        var ref = audit.recordPipelineCompleted(context);
        assertNotNull(ref);
    }

    @Test
    void shouldClearAuditLog() {
        audit.recordRequestReceived(context);
        audit.clear();
        assertTrue(audit.getAuditLog().isEmpty());
    }
}
