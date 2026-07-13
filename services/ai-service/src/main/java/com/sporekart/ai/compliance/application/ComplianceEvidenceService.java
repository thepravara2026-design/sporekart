package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceEvidence;

import java.util.Map;
import java.util.UUID;

public interface ComplianceEvidenceService {
    ComplianceEvidence collectEvidence(UUID assessmentId, String evidenceType, String source, Map<String, Object> data);
    ComplianceEvidence verifyEvidence(UUID evidenceId, boolean verified, UUID verifiedBy);
    boolean verifyEvidenceIntegrity(UUID evidenceId);
}
