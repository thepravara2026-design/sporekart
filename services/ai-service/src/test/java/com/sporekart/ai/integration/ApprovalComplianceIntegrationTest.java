package com.sporekart.ai.integration;

import com.sporekart.ai.approval.api.ApprovalEngine;
import com.sporekart.ai.approval.domain.ApprovalRequest;
import com.sporekart.ai.approval.domain.ApprovalStatus;
import com.sporekart.ai.compliance.api.ComplianceEngine;
import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
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
class ApprovalComplianceIntegrationTest {

    @Mock private ApprovalEngine approvalEngine;
    @Mock private ComplianceEngine complianceEngine;

    @Test
    void testApprovedRequestPassesToCompliance() {
        UUID requestId = UUID.randomUUID();
        UUID approvalId = UUID.randomUUID();

        ApprovalRequest approvedRequest = new ApprovalRequest(
            approvalId, "catalog", "publish_item",
            Map.of("itemId", "P100"), Map.of("priority", "high"),
            "user1", List.of("editor"), "Approved by manager",
            "high", requestId, Map.of(),
            OffsetDateTime.now().plusDays(1),
            ApprovalStatus.APPROVED, OffsetDateTime.now()
        );

        when(approvalEngine.getStatus(approvalId)).thenReturn(approvedRequest);
        when(approvalEngine.approve(approvalId, "reviewer1", "Approved")).thenReturn(approvedRequest);

        ComplianceRequest compRequest = new ComplianceRequest(
            approvedRequest.module(), approvedRequest.action(),
            Map.of("approvalId", approvalId.toString(),
                   "decisionId", requestId.toString(),
                   "payload", approvedRequest.payload()),
            approvedRequest.userId(), approvedRequest.roles()
        );

        ComplianceResult compResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(),
            Map.of("approved", true, "framework", "GDPR")
        );
        when(complianceEngine.validate(compRequest)).thenReturn(compResult);

        ApprovalRequest approvalResult = approvalEngine.approve(approvalId, "reviewer1", "Approved");
        assertEquals(ApprovalStatus.APPROVED, approvalResult.status());

        ComplianceResult complianceResult = complianceEngine.validate(compRequest);
        assertNotNull(complianceResult);
        assertTrue(complianceResult.compliant());
        assertEquals(ComplianceStatus.PASSED, complianceResult.status());

        verify(approvalEngine).approve(approvalId, "reviewer1", "Approved");
        verify(complianceEngine).validate(compRequest);
    }

    @Test
    void testRejectedRequestDoesNotTriggerCompliance() {
        UUID approvalId = UUID.randomUUID();

        ApprovalRequest rejectedRequest = new ApprovalRequest(
            approvalId, "catalog", "delete_item",
            Map.of("itemId", "P200"), Map.of(), "user2",
            List.of("editor"), "Not approved", "low",
            UUID.randomUUID(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.REJECTED, OffsetDateTime.now()
        );
        when(approvalEngine.reject(approvalId, "reviewer2", "Policy violation"))
            .thenReturn(rejectedRequest);

        ApprovalRequest result = approvalEngine.reject(approvalId, "reviewer2", "Policy violation");
        assertEquals(ApprovalStatus.REJECTED, result.status());

        verify(approvalEngine).reject(approvalId, "reviewer2", "Policy violation");
        verify(complianceEngine, never()).validate(any());
    }

    @Test
    void testComplianceValidationAfterApprovalRecordsResult() {
        UUID approvalId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        ApprovalRequest approvedRequest = new ApprovalRequest(
            approvalId, "finance", "process_payment",
            Map.of("amount", 5000.0), Map.of("currency", "USD"),
            "user3", List.of("finance_admin"), "Approved payment",
            "medium", requestId, Map.of(),
            OffsetDateTime.now().plusHours(12),
            ApprovalStatus.APPROVED, OffsetDateTime.now()
        );

        ComplianceRequest compRequest = new ComplianceRequest(
            "finance", "process_payment",
            Map.of("approvalId", approvalId.toString(),
                   "amount", 5000.0, "currency", "USD"),
            "user3", List.of("finance_admin")
        );

        ComplianceResult compResult = new ComplianceResult(
            false, ComplianceStatus.FAILED,
            List.of(), List.of(),
            UUID.randomUUID(), UUID.randomUUID(),
            Map.of("reason", "Amount exceeds single transaction limit", "limit", 3000.0)
        );

        when(approvalEngine.getStatus(approvalId)).thenReturn(approvedRequest);
        when(complianceEngine.validate(compRequest)).thenReturn(compResult);

        ApprovalRequest retrieved = approvalEngine.getStatus(approvalId);
        assertEquals(ApprovalStatus.APPROVED, retrieved.status());

        ComplianceResult validationResult = complianceEngine.validate(compRequest);
        assertFalse(validationResult.compliant());
        assertEquals(ComplianceStatus.FAILED, validationResult.status());
        assertNotNull(validationResult.details());
        assertTrue(validationResult.details().containsKey("reason"));

        verify(complianceEngine).validate(compRequest);
    }
}
