package com.sporekart.ai.approval.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;

class ApprovalAssignmentTest {

    @Test
    void shouldConstructApprovalAssignment() {
        var id = UUID.randomUUID();
        var requestId = UUID.randomUUID();
        var reviewerId = UUID.randomUUID();
        var assignedAt = OffsetDateTime.now();
        var deadline = OffsetDateTime.now().plusDays(1);

        var assignment = new ApprovalAssignment(
            id, requestId, reviewerId, "reviewer1",
            AssignmentStrategy.ROLE_BASED, 1, ApprovalStatus.ASSIGNED,
            assignedAt, null, deadline
        );

        assertEquals(id, assignment.id());
        assertEquals(requestId, assignment.requestId());
        assertEquals(reviewerId, assignment.reviewerId());
        assertEquals("reviewer1", assignment.reviewerUserId());
        assertEquals(AssignmentStrategy.ROLE_BASED, assignment.strategy());
        assertEquals(1, assignment.level());
        assertEquals(ApprovalStatus.ASSIGNED, assignment.status());
        assertEquals(assignedAt, assignment.assignedAt());
        assertNull(assignment.respondedAt());
        assertEquals(deadline, assignment.deadline());
    }

    @Test
    void shouldCreateAssignmentWithRespondedAt() {
        var respondedAt = OffsetDateTime.now();
        var assignment = new ApprovalAssignment(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "reviewer1",
            AssignmentStrategy.DIRECT, 2, ApprovalStatus.APPROVED,
            OffsetDateTime.now(), respondedAt, null
        );

        assertEquals(respondedAt, assignment.respondedAt());
        assertEquals(ApprovalStatus.APPROVED, assignment.status());
    }
}
