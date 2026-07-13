package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceEvidenceService {
    ComplianceEvidence collectEvidence(UUID assessmentId, String evidenceType, String source, Map<String, Object> data);
    ComplianceEvidence getEvidence(UUID id);
    List<ComplianceEvidence> getEvidenceByAssessment(UUID assessmentId);
    ComplianceEvidence verifyEvidence(UUID id, UUID verifiedBy, boolean verified);
    boolean verifyEvidenceIntegrity(UUID id);
}
