package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.AssessmentStatus;
import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceAssessmentServiceImpl implements ComplianceAssessmentService {

    private final ComplianceAssessmentRepository assessmentRepository;
    private final ComplianceEvidenceService complianceEvidenceService;
    private final ComplianceAuditService complianceAuditService;

    @Override
    public ComplianceAssessment createAssessment(UUID frameworkId, String module, String action, Map<String, Object> context) {
        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(),
            frameworkId,
            module,
            action,
            AssessmentStatus.PLANNED,
            null,
            context != null ? context : Map.of(),
            null,
            Instant.now(),
            null
        );
        ComplianceAssessment saved = assessmentRepository.save(assessment);
        log.info("Created assessment {} for framework {} module {}", saved.id(), frameworkId, module);

        complianceAuditService.recordAudit(
            "ASSESSMENT_CREATE", "ASSESSMENT", saved.id(), null,
            Map.of("frameworkId", frameworkId.toString(), "module", module, "action", action),
            "assessment"
        );

        return saved;
    }

    @Override
    public ComplianceAssessment completeAssessment(UUID assessmentId, ComplianceStatus result) {
        ComplianceAssessment existing = assessmentRepository.findById(assessmentId)
            .orElseThrow(() -> new RuntimeException("Assessment not found: " + assessmentId));

        ComplianceAssessment completed = new ComplianceAssessment(
            existing.id(),
            existing.frameworkId(),
            existing.module(),
            existing.action(),
            AssessmentStatus.COMPLETED,
            result,
            existing.context(),
            existing.reviewerId(),
            existing.assessedAt(),
            Instant.now()
        );
        ComplianceAssessment saved = assessmentRepository.save(completed);
        log.info("Completed assessment {} with status {}", saved.id(), result);

        complianceAuditService.recordAudit(
            "ASSESSMENT_COMPLETE", "ASSESSMENT", saved.id(), null,
            Map.of("status", result.name()),
            "assessment"
        );

        return saved;
    }
}
