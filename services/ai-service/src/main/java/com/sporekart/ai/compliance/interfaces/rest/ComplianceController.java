package com.sporekart.ai.compliance.interfaces.rest;

import com.sporekart.ai.compliance.api.*;
import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.interfaces.rest.dto.*;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.compliance.infrastructure.monitoring.ComplianceMonitoringService;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceExceptionRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceEngine complianceEngine;
    private final ComplianceRegistry complianceRegistry;
    private final ComplianceReportingService complianceReportingService;
    private final ComplianceViolationRepository complianceViolationRepository;
    private final ComplianceExceptionRepository complianceExceptionRepository;
    private final ComplianceMetricsService complianceMetricsService;
    private final ComplianceKafkaEventPublisher complianceKafkaEventPublisher;
    private final ComplianceMonitoringService complianceMonitoringService;
    private final ComplianceHealthService complianceHealthService;

    @PostMapping("/validate")
    public ResponseEntity<ComplianceValidateResponse> validate(@RequestBody ComplianceValidateRequest request) {
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

        ComplianceValidateResponse response = new ComplianceValidateResponse(
            result.id().toString(),
            result.compliant(),
            result.status().name(),
            result.violations().stream().map(ComplianceViolation::description).toList(),
            result.findings().stream().map(ComplianceFinding::description).toList(),
            result.message(),
            result.timestamp().toEpochMilli()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/frameworks")
    public ResponseEntity<List<FrameworkDto>> listFrameworks() {
        List<FrameworkDto> frameworks = complianceRegistry.findAllFrameworks().stream()
            .map(fw -> new FrameworkDto(
                fw.id().toString(),
                fw.name(),
                fw.version(),
                fw.type().name(),
                fw.description(),
                fw.authority(),
                fw.active()
            )).toList();
        return ResponseEntity.ok(frameworks);
    }

    @GetMapping("/rules")
    public ResponseEntity<List<RuleDto>> listRules(@RequestParam(required = false) UUID frameworkId) {
        List<ComplianceRule> rules;
        if (frameworkId != null) {
            rules = complianceRegistry.findRulesByFramework(frameworkId);
        } else {
            rules = complianceRegistry.findActiveRules();
        }

        List<RuleDto> ruleDtos = rules.stream()
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
        return ResponseEntity.ok(ruleDtos);
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> listReports() {
        List<ReportDto> reports = complianceRegistry.findAllFrameworks().stream()
            .flatMap(fw -> complianceReportingService.getReportsByFramework(fw.id()).stream())
            .map(r -> new ReportDto(
                r.id().toString(),
                r.frameworkId().toString(),
                r.title(),
                r.overallStatus().name(),
                r.summary(),
                r.generatedAt().toString()
            )).toList();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/violations")
    public ResponseEntity<List<ViolationDto>> listViolations() {
        List<ViolationDto> violations = complianceViolationRepository.findAll().stream()
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
        return ResponseEntity.ok(violations);
    }

    @GetMapping("/exceptions")
    public ResponseEntity<List<ExceptionResponse>> listExceptions() {
        List<ExceptionResponse> exceptions = complianceExceptionRepository.findAll().stream()
            .map(e -> new ExceptionResponse(
                e.id().toString(),
                e.ruleId().toString(),
                e.reason(),
                e.justification(),
                e.status().name(),
                e.expiresAt() != null ? e.expiresAt().toString() : null
            )).toList();
        return ResponseEntity.ok(exceptions);
    }

    @PostMapping("/exceptions")
    public ResponseEntity<ExceptionResponse> createException(@RequestBody ExceptionRequest request) {
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

        ExceptionResponse response = new ExceptionResponse(
            saved.id().toString(),
            saved.ruleId().toString(),
            saved.reason(),
            saved.justification(),
            saved.status().name(),
            saved.expiresAt() != null ? saved.expiresAt().toString() : null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<ComplianceStatsDto> getStatistics() {
        ComplianceStatsDto stats = new ComplianceStatsDto(
            complianceMetricsService.getTotalValidations(),
            complianceMetricsService.getPassCount(),
            complianceMetricsService.getFailureCount(),
            complianceMetricsService.getViolationCount(),
            complianceMetricsService.getPassRate(),
            complianceMetricsService.getAverageAssessmentLatencyMs(),
            complianceMetricsService.getStatistics()
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDto> health() {
        Map<String, Object> healthDetails = complianceHealthService.getHealthDetails();
        HealthDto healthDto = new HealthDto(
            (String) healthDetails.getOrDefault("status", "DOWN"),
            (String) healthDetails.getOrDefault("service", "compliance"),
            System.currentTimeMillis(),
            healthDetails
        );
        return ResponseEntity.ok(healthDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }
}
