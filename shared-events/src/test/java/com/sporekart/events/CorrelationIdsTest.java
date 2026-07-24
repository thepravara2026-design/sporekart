package com.sporekart.events;

import com.sporekart.events.model.CorrelationIds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdsTest {

    @Test
    void testNewId() {
        CorrelationIds ids = CorrelationIds.newId();
        assertNotNull(ids.getCorrelationId());
        assertNotNull(ids.getCausationId());
        assertNotNull(ids.getTraceId());
        assertEquals(ids.getCorrelationId(), ids.getCausationId());
    }

    @Test
    void testBuilder() {
        CorrelationIds ids = CorrelationIds.builder()
                .correlationId("corr-1")
                .causationId("cause-1")
                .traceId("trace-1")
                .requestId("req-1")
                .workflowId("wf-1")
                .conversationId("conv-1")
                .build();

        assertEquals("corr-1", ids.getCorrelationId());
        assertEquals("cause-1", ids.getCausationId());
        assertEquals("trace-1", ids.getTraceId());
        assertEquals("req-1", ids.getRequestId());
        assertEquals("wf-1", ids.getWorkflowId());
        assertEquals("conv-1", ids.getConversationId());
    }

    @Test
    void testFromCausation() {
        CorrelationIds parent = CorrelationIds.builder()
                .correlationId("corr-1")
                .causationId("cause-1")
                .traceId("trace-1")
                .build();

        CorrelationIds child = CorrelationIds.fromCausation(parent);
        assertEquals(parent.getCorrelationId(), child.getCorrelationId());
        assertEquals(parent.getCausationId(), child.getCausationId());
        assertEquals(parent.getTraceId(), child.getTraceId());
    }

    @Test
    void testUniqueIds() {
        CorrelationIds ids1 = CorrelationIds.newId();
        CorrelationIds ids2 = CorrelationIds.newId();
        assertNotEquals(ids1.getCorrelationId(), ids2.getCorrelationId());
    }
}
