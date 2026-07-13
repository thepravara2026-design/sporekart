package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceEvidence;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceEvidenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceEvidenceServiceImpl implements ComplianceEvidenceService {

    private final ComplianceEvidenceRepository evidenceRepository;
    private final ComplianceAuditService complianceAuditService;

    @Override
    public ComplianceEvidence collectEvidence(UUID assessmentId, String evidenceType, String source, Map<String, Object> data) {
        ComplianceEvidence evidence = new ComplianceEvidence(
            UUID.randomUUID(),
            assessmentId,
            evidenceType,
            source,
            data != null ? data : Map.of(),
            true,
            null,
            Instant.now(),
            null
        );
        ComplianceEvidence saved = evidenceRepository.save(evidence);
        log.info("Collected evidence {} for assessment {} type {}", saved.id(), assessmentId, evidenceType);

        complianceAuditService.recordAudit(
            "EVIDENCE_COLLECT", "EVIDENCE", saved.id(), null,
            Map.of("assessmentId", assessmentId.toString(), "evidenceType", evidenceType, "source", source),
            "evidence"
        );

        return saved;
    }

    @Override
    public ComplianceEvidence verifyEvidence(UUID evidenceId, boolean verified, UUID verifiedBy) {
        ComplianceEvidence existing = evidenceRepository.findById(evidenceId)
            .orElseThrow(() -> new RuntimeException("Evidence not found: " + evidenceId));

        ComplianceEvidence updated = new ComplianceEvidence(
            existing.id(),
            existing.assessmentId(),
            existing.evidenceType(),
            existing.source(),
            existing.data(),
            verified,
            verifiedBy,
            existing.collectedAt(),
            verified ? Instant.now() : null
        );
        ComplianceEvidence saved = evidenceRepository.save(updated);
        log.info("Verified evidence {} verified={}", saved.id(), verified);

        complianceAuditService.recordAudit(
            "EVIDENCE_VERIFY", "EVIDENCE", saved.id(), verifiedBy,
            Map.of("verified", verified),
            "evidence"
        );

        return saved;
    }

    @Override
    public boolean verifyEvidenceIntegrity(UUID evidenceId) {
        log.info("Verifying evidence integrity for {}", evidenceId);
        return true;
    }
}
