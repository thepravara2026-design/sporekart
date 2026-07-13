package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class ApprovalRequestTest {

    @Test
    void shouldConstructApprovalRequestWithAllFields() {
        var id = UUID.randomUUID();
        var deadline = OffsetDateTime.now().plusDays(1);
        var createdAt = OffsetDateTime.now();
        var payload = Map.of("key", "value");
        var context = Map.of("env", "prod");
        var roles = List.of("admin");
        var metadata = Map.of("source", "web");

        var request = new ApprovalRequest(
            id, "content", "publish", payload, context, "user1", roles,
            "approval needed", "normal", UUID.randomUUID(), metadata,
            deadline, ApprovalStatus.PENDING, createdAt
        );

        assertEquals(id, request.id());
        assertEquals("content", request.module());
        assertEquals("publish", request.action());
        assertEquals(payload, request.payload());
        assertEquals(context, request.context());
        assertEquals("user1", request.userId());
        assertEquals(roles, request.roles());
        assertEquals("approval needed", request.reason());
        assertEquals("normal", request.urgency());
        assertNotNull(request.decisionId());
        assertEquals(metadata, request.metadata());
        assertEquals(deadline, request.deadline());
        assertEquals(ApprovalStatus.PENDING, request.status());
        assertEquals(createdAt, request.createdAt());
    }

    @Test
    void shouldConstructWithUrgentPriority() {
        var request = new ApprovalRequest(
            UUID.randomUUID(), "content", "publish", Map.of(), Map.of(),
            "user1", List.of("admin"), "urgent reason", "urgent", null, Map.of(),
            OffsetDateTime.now().plusHours(1), ApprovalStatus.PENDING, OffsetDateTime.now()
        );

        assertEquals("urgent", request.urgency());
    }
}
