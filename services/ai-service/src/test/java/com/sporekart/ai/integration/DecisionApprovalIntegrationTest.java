package com.sporekart.ai.integration;

import com.sporekart.ai.decision.api.DecisionEngine;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.approval.api.ApprovalEngine;
import com.sporekart.ai.approval.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionApprovalIntegrationTest {

    @Mock private DecisionEngine decisionEngine;
    @Mock private ApprovalEngine approvalEngine;

    @Test
    void testDecisionTriggersApprovalWorkflow() {
        UUID requestId = UUID.randomUUID();
        DecisionRequest decRequest = new DecisionRequest(
            requestId, "finance", "large_transfer",
            Map.of("amount", 100000.0), Map.of(), "user1",
            List.of("finance_user"), List.of(), List.of(),
            Map.of(), OffsetDateTime.now()
        );

        DecisionResult decResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.REQUIRE_APPROVAL,
            DecisionStatus.PENDING, DecisionConfidence.MEDIUM,
            "Large transfer requires manager approval",
            List.of(new DecisionReason(UUID.randomUUID(), "HIGH_VALUE",
                "Amount exceeds auto-approval limit", "policy",
                DecisionConfidence.MEDIUM, Map.of("limit", 50000))),
            List.of(), null, 7L, true, true, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(decRequest)).thenReturn(decResult);

        ApprovalRequest approvalRequest = new ApprovalRequest(
            UUID.randomUUID(), "finance", "large_transfer",
            Map.of("amount", 100000.0),
            Map.of("decisionId", decResult.id().toString(),
                   "confidence", decResult.confidence().name()),
            "user1", List.of("finance_user"),
            "Large transfer requires approval", "high",
            decResult.id(), Map.of("requiresUrgent", true),
            OffsetDateTime.now().plusHours(24),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );

        ApprovalRequest submittedRequest = new ApprovalRequest(
            approvalRequest.id(), approvalRequest.module(),
            approvalRequest.action(), approvalRequest.payload(),
            approvalRequest.context(), approvalRequest.userId(),
            approvalRequest.roles(), approvalRequest.reason(),
            approvalRequest.urgency(), approvalRequest.decisionId(),
            approvalRequest.metadata(), approvalRequest.deadline(),
            ApprovalStatus.PENDING, approvalRequest.createdAt()
        );
        when(approvalEngine.submit(any(ApprovalRequest.class))).thenReturn(submittedRequest);

        DecisionResult actualDec = decisionEngine.evaluate(decRequest);
        assertTrue(actualDec.requiresApproval());
        assertEquals(DecisionAction.REQUIRE_APPROVAL, actualDec.action());

        ApprovalRequest submitted = approvalEngine.submit(approvalRequest);
        assertNotNull(submitted);
        assertEquals(ApprovalStatus.PENDING, submitted.status());
        assertEquals(decResult.id(), submitted.decisionId());

        verify(decisionEngine).evaluate(decRequest);
        verify(approvalEngine).submit(any(ApprovalRequest.class));
    }

    @Test
    void testApproveFlowEndToEnd() {
        UUID requestId = UUID.randomUUID();
        UUID approvalId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();

        ApprovalRequest pendingRequest = new ApprovalRequest(
            approvalId, "catalog", "publish_item",
            Map.of("itemId", "456"), Map.of(), "user2",
            List.of("editor"), "Publish new item", "medium",
            requestId, Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );
        when(approvalEngine.getStatus(approvalId)).thenReturn(pendingRequest);

        ApprovalRequest approvedRequest = new ApprovalRequest(
            approvalId, "catalog", "publish_item",
            Map.of("itemId", "456"), Map.of(), "user2",
            List.of("editor"), "Publish new item", "medium",
            requestId, Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.APPROVED, OffsetDateTime.now()
        );
        when(approvalEngine.approve(approvalId, reviewerId.toString(), "Looks good")).thenReturn(approvedRequest);

        ApprovalRequest statusCheck = approvalEngine.getStatus(approvalId);
        assertEquals(ApprovalStatus.PENDING, statusCheck.status());

        ApprovalRequest result = approvalEngine.approve(approvalId, reviewerId.toString(), "Looks good");
        assertNotNull(result);
        assertEquals(ApprovalStatus.APPROVED, result.status());

        verify(approvalEngine).getStatus(approvalId);
        verify(approvalEngine).approve(approvalId, reviewerId.toString(), "Looks good");
    }

    @Test
    void testRejectFlowEndToEnd() {
        UUID approvalId = UUID.randomUUID();
        UUID reviewerId = UUID.randomUUID();

        ApprovalRequest pendingRequest = new ApprovalRequest(
            approvalId, "catalog", "delete_item",
            Map.of("itemId", "789"), Map.of(), "user3",
            List.of("editor"), "Delete outdated item", "low",
            UUID.randomUUID(), Map.of(), OffsetDateTime.now().plusDays(3),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );
        when(approvalEngine.getStatus(approvalId)).thenReturn(pendingRequest);

        ApprovalRequest rejectedRequest = new ApprovalRequest(
            approvalId, "catalog", "delete_item",
            Map.of("itemId", "789"), Map.of(), "user3",
            List.of("editor"), "Delete outdated item", "low",
            UUID.randomUUID(), Map.of(), OffsetDateTime.now().plusDays(3),
            ApprovalStatus.REJECTED, OffsetDateTime.now()
        );
        when(approvalEngine.reject(approvalId, reviewerId.toString(), "Item still in use"))
            .thenReturn(rejectedRequest);

        ApprovalRequest result = approvalEngine.reject(approvalId, reviewerId.toString(), "Item still in use");
        assertEquals(ApprovalStatus.REJECTED, result.status());

        verify(approvalEngine).reject(approvalId, reviewerId.toString(), "Item still in use");
    }

    @Test
    void testDecisionWithoutApprovalProceedsDirectly() {
        UUID requestId = UUID.randomUUID();
        DecisionRequest decRequest = new DecisionRequest(
            requestId, "catalog", "view_item",
            Map.of("itemId", "101"), Map.of(), "user4",
            List.of("public"), List.of(), List.of(),
            Map.of(), OffsetDateTime.now()
        );

        DecisionResult decResult = new DecisionResult(
            UUID.randomUUID(), requestId, DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.CERTAIN,
            "Public view allowed", List.of(), List.of(), null,
            2L, false, false, OffsetDateTime.now()
        );
        when(decisionEngine.evaluate(decRequest)).thenReturn(decResult);

        DecisionResult actualDec = decisionEngine.evaluate(decRequest);
        assertFalse(actualDec.requiresApproval());
        assertEquals(DecisionAction.ALLOW, actualDec.action());
        assertEquals(DecisionStatus.ALLOWED, actualDec.status());

        verify(decisionEngine).evaluate(decRequest);
        verify(approvalEngine, never()).submit(any());
    }
}
