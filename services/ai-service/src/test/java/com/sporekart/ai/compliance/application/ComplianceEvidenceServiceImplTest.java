package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceEvidence;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceEvidenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceEvidenceServiceImplTest {

    @Mock private ComplianceEvidenceRepository evidenceRepository;
    @Mock private ComplianceAuditService complianceAuditService;

    private ComplianceEvidenceServiceImpl evidenceService;

    @BeforeEach
    void setUp() {
        evidenceService = new ComplianceEvidenceServiceImpl(evidenceRepository, complianceAuditService);
    }

    @Test
    void testCollectEvidence() {
        UUID assessmentId = UUID.randomUUID();
        String evidenceType = "ENGINE";
        String source = "validation";
        Map<String, Object> data = Map.of("key", "value");

        when(evidenceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceEvidence evidence = evidenceService.collectEvidence(assessmentId, evidenceType, source, data);

        assertNotNull(evidence);
        assertEquals(assessmentId, evidence.assessmentId());
        assertEquals(evidenceType, evidence.evidenceType());
        assertEquals(source, evidence.source());
        assertTrue(evidence.verified());
        verify(complianceAuditService).recordAudit(
            eq("EVIDENCE_COLLECT"), eq("EVIDENCE"), any(), isNull(), anyMap(), eq("evidence")
        );
    }

    @Test
    void testCollectEvidenceWithNullData() {
        UUID assessmentId = UUID.randomUUID();

        when(evidenceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceEvidence evidence = evidenceService.collectEvidence(assessmentId, "TYPE", "source", null);

        assertNotNull(evidence.data());
        assertTrue(evidence.data().isEmpty());
    }

    @Test
    void testVerifyEvidence() {
        UUID evidenceId = UUID.randomUUID();
        UUID verifiedBy = UUID.randomUUID();

        ComplianceEvidence existing = new ComplianceEvidence(
            evidenceId, UUID.randomUUID(), "TYPE", "source",
            Map.of(), false, null, Instant.now(), null
        );
        when(evidenceRepository.findById(evidenceId)).thenReturn(Optional.of(existing));
        when(evidenceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ComplianceEvidence result = evidenceService.verifyEvidence(evidenceId, true, verifiedBy);

        assertTrue(result.verified());
        assertEquals(verifiedBy, result.verifiedBy());
        assertNotNull(result.verifiedAt());
        verify(complianceAuditService).recordAudit(
            eq("EVIDENCE_VERIFY"), eq("EVIDENCE"), eq(evidenceId), eq(verifiedBy), anyMap(), eq("evidence")
        );
    }

    @Test
    void testVerifyEvidenceThrowsWhenNotFound() {
        UUID evidenceId = UUID.randomUUID();
        when(evidenceRepository.findById(evidenceId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            evidenceService.verifyEvidence(evidenceId, true, UUID.randomUUID()));
    }

    @Test
    void testVerifyEvidenceIntegrity() {
        UUID evidenceId = UUID.randomUUID();
        boolean result = evidenceService.verifyEvidenceIntegrity(evidenceId);
        assertTrue(result);
    }
}
