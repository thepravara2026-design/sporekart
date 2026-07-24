package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.api.ComplianceEngine;
import com.sporekart.ai.compliance.api.ComplianceHealthService;
import com.sporekart.ai.compliance.api.ComplianceMetricsService;
import com.sporekart.ai.compliance.api.ComplianceRegistry;
import com.sporekart.ai.compliance.api.ComplianceReportingService;
import com.sporekart.ai.compliance.domain.ComplianceException;
import com.sporekart.ai.compliance.domain.ComplianceFinding;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.domain.ExceptionStatus;
import com.sporekart.ai.compliance.engine.ComplianceRequest;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.compliance.infrastructure.monitoring.ComplianceMonitoringService;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceExceptionRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import com.sporekart.ai.compliance.interfaces.rest.dto.ComplianceStatsDto;
import com.sporekart.ai.compliance.interfaces.rest.dto.ComplianceValidateRequest;
import com.sporekart.ai.compliance.interfaces.rest.dto.ComplianceValidateResponse;
import com.sporekart.ai.compliance.interfaces.rest.dto.ExceptionRequest;
import com.sporekart.ai.compliance.interfaces.rest.dto.ExceptionResponse;
import com.sporekart.ai.compliance.interfaces.rest.dto.FrameworkDto;
import com.sporekart.ai.compliance.interfaces.rest.dto.HealthDto;
import com.sporekart.ai.compliance.interfaces.rest.dto.ReportDto;
import com.sporekart.ai.compliance.interfaces.rest.dto.RuleDto;
import com.sporekart.ai.compliance.interfaces.rest.dto.ViolationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final ComplianceEngine complianceEngine;
    private final ComplianceRegistry complianceRegistry;
    private final ComplianceReportingService complianceReportingService;
    private final ComplianceViolationRepository complianceViolationRepository;
    private final ComplianceExceptionRepository complianceExceptionRepository;
    private final ComplianceMetricsService complianceMetricsService;
    private final ComplianceKafkaEventPublisher complianceKafkaEventPublisher;
    private final ComplianceMonitoringService complianceMonitoringService;
    private final ComplianceHealthService complianceHealthService;

    public ComplianceValidateResponse validate(ComplianceValidateRequest request) {
        complianceMonitoringService.recordRequest();
        long start = System.currentTimeMillis();

        ComplianceRequest domainRequest = new ComplianceRequest(
            UUID.randomUUID(),
            request.module(),
            request.action(),
            request.context() != null ? request.context() : new HashMap<>(),
            request.userId(),
            request.roles() != null ? request.roles() : List.of(),
            OffsetDateTime.now()
        );

        ComplianceResult result = complianceEngine.validate(domainRequest);

        long elapsed = System.currentTimeMillis() - start;
        complianceMonitoringService.recordValidation(elapsed);
        complianceMonitoringService.recordDecision(result.status().name());

        complianceKafkaEventPublisher.publishValidationEvent(
            domainRequest.id().toString(),
            result.status().name(),
            elapsed
        );

        return new ComplianceValidateResponse(
            result.id().toString(),
            result.compliant(),
            result.status().name(),
            result.violations().stream().map(ComplianceViolation::description).toList(),
            result.findings().stream().map(ComplianceFinding::description).toList(),
            result.message(),
            result.timestamp().toEpochMilli()
        );
    }

    public List<FrameworkDto> listFrameworks() {
        return complianceRegistry.findAllFrameworks().stream()
            .map(fw -> new FrameworkDto(
                fw.id().toString(),
                fw.name(),
                fw.version(),
                fw.type().name(),
                fw.description(),
                fw.authority(),
                fw.active()
            )).toList();
    }

    public List<RuleDto> listRules(UUID frameworkId) {
        List<ComplianceRule> rules;
        if (frameworkId != null) {
            rules = complianceRegistry.findRulesByFramework(frameworkId);
        } else {
            rules = complianceRegistry.findActiveRules();
        }

        return rules.stream()
            .map(r -> new RuleDto(
                r.id().toString(),
                r.frameworkId().toString(),
                r.ruleId(),
                r.name(),
                r.description(),
                r.category(),
                r.riskLevel().name(),
                r.active()
            )).toList();
    }

    public List<ReportDto> listReports() {
        return complianceRegistry.findAllFrameworks().stream()
            .flatMap(fw -> complianceReportingService.getReportsByFramework(fw.id()).stream())
            .map(r -> new ReportDto(
                r.id().toString(),
                r.frameworkId().toString(),
                r.title(),
                r.overallStatus().name(),
                r.summary(),
                r.generatedAt().toString()
            )).toList();
    }

    public List<ViolationDto> listViolations() {
        return complianceViolationRepository.findAll().stream()
            .map(v -> new ViolationDto(
                v.id().toString(),
                v.ruleId().toString(),
                v.module(),
                v.severity().name(),
                v.description(),
                v.details(),
                v.remediated(),
                v.detectedAt().toString()
            )).toList();
    }

    public List<ExceptionResponse> listExceptions() {
        return complianceExceptionRepository.findAll().stream()
            .map(e -> new ExceptionResponse(
                e.id().toString(),
                e.ruleId().toString(),
                e.reason(),
                e.justification(),
                e.status().name(),
                e.expiresAt() != null ? e.expiresAt().toString() : null
            )).toList();
    }

    public ExceptionResponse createException(ExceptionRequest request) {
        ComplianceException exception = new ComplianceException(
            UUID.randomUUID(),
            UUID.fromString(request.ruleId()),
            null,
            request.reason(),
            request.justification(),
            request.requestedBy(),
            ExceptionStatus.REQUESTED,
            null,
            Instant.now(),
            null,
            Instant.now().plusSeconds(7 * 86400)
        );

        ComplianceException saved = complianceExceptionRepository.save(exception);

        complianceMonitoringService.recordExceptionCreated();

        return new ExceptionResponse(
            saved.id().toString(),
            saved.ruleId().toString(),
            saved.reason(),
            saved.justification(),
            saved.status().name(),
            saved.expiresAt() != null ? saved.expiresAt().toString() : null
        );
    }

    public ComplianceStatsDto getStatistics() {
        return new ComplianceStatsDto(
            complianceMetricsService.getTotalValidations(),
            complianceMetricsService.getPassCount(),
            complianceMetricsService.getFailureCount(),
            complianceMetricsService.getViolationCount(),
            complianceMetricsService.getPassRate(),
            complianceMetricsService.getAverageAssessmentLatencyMs(),
            complianceMetricsService.getStatistics()
        );
    }

    public HealthDto getHealth() {
        Map<String, Object> healthDetails = complianceHealthService.getHealthDetails();
        return new HealthDto(
            (String) healthDetails.getOrDefault("status", "DOWN"),
            (String) healthDetails.getOrDefault("service", "compliance"),
            System.currentTimeMillis(),
            healthDetails
        );
    }
}
