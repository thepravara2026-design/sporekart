package com.sporekart.ai.policy.interfaces.rest;

import com.sporekart.ai.policy.api.*;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.interfaces.rest.dto.*;
import com.sporekart.ai.policy.infrastructure.kafka.PolicyKafkaEventPublisher;
import com.sporekart.ai.policy.infrastructure.monitoring.PolicyMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyEngine engine;
    private final PolicyResolver resolver;
    private final PolicyLifecycleManager lifecycleManager;
    private final PolicyAuditService auditService;
    private final PolicyMetricsService metricsService;
    private final PolicyKafkaEventPublisher eventPublisher;
    private final PolicyMonitoringService monitoringService;

    @GetMapping
    public ResponseEntity<PolicyListDto> listPolicies(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String scope) {
        List<Policy> policies;
        if (module != null) {
            policies = resolver.resolvePoliciesByModule(module);
        } else if (scope != null) {
            policies = resolver.resolvePoliciesByScope(PolicyScope.valueOf(scope.toUpperCase()));
        } else {
            policies = resolver.resolveActivePolicies();
        }
        List<PolicyResponseDto> dtos = policies.stream().map(this::toDto).toList();
        return ResponseEntity.ok(new PolicyListDto(dtos, dtos.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponseDto> getPolicy(@PathVariable UUID id) {
        Policy policy = resolver.resolvePolicy(id);
        if (policy == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(policy));
    }

    @PostMapping
    public ResponseEntity<PolicyResponseDto> createPolicy(@RequestBody PolicyRequestDto request) {
        Policy policy = new Policy(
            UUID.randomUUID(), request.name(), request.description(),
            PolicyType.valueOf(request.type().toUpperCase()),
            PolicyStatus.DRAFT, PolicySeverity.INFO,
            PolicyScope.valueOf(request.scope().toUpperCase()),
            request.priority(), request.module(),
            new ArrayList<>(), new ArrayList<>(),
            new HashMap<>(), request.active(), false,
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            OffsetDateTime.now(), OffsetDateTime.now()
        );
        eventPublisher.publishCreated(policy.id().toString(), policy.name());
        monitoringService.recordActivation();
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponseDto> updatePolicy(@PathVariable UUID id, @RequestBody PolicyRequestDto request) {
        Policy policy = resolver.resolvePolicy(id);
        if (policy == null) {
            return ResponseEntity.notFound().build();
        }
        eventPublisher.publishUpdated(id.toString(), request.name());
        return ResponseEntity.ok(new PolicyResponseDto(
            id.toString(), request.name(), request.description(),
            request.type(), policy.status().name(), request.scope(),
            request.priority(), request.module(), request.active(), Map.of()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        lifecycleManager.archivePolicy(id, "api");
        eventPublisher.publishDeleted(id.toString());
        monitoringService.recordDeactivation();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResultDto> evaluate(@RequestBody EvaluationRequestDto request) {
        monitoringService.recordEvaluation(0);
        long start = System.currentTimeMillis();

        EvaluationRequest domainRequest = new EvaluationRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload() != null ? request.payload() : new HashMap<>(),
            request.context() != null ? request.context() : new HashMap<>(),
            request.userId(), request.roles() != null ? request.roles() : List.of(),
            new HashMap<>(), OffsetDateTime.now()
        );

        EvaluationResult result = engine.evaluate(domainRequest);
        long elapsed = System.currentTimeMillis() - start;

        monitoringService.recordDecision(result.finalDecision().name());
        eventPublisher.publishEvaluated(domainRequest.id().toString(), result.finalDecision().name(), elapsed);

        return ResponseEntity.ok(new EvaluationResultDto(
            result.requestId().toString(),
            result.finalDecision().name(),
            result.violations().stream()
                .map(v -> new ViolationDto(
                    v.ruleName(), v.message(), v.severity().name(),
                    v.details(), v.overridable()
                )).toList(),
            result.totalEvaluationTimeMs(), result.passed()
        ));
    }

    @GetMapping("/evaluations")
    public ResponseEntity<EvaluationListDto> getEvaluations(
            @RequestParam(required = false) String decision) {
        List<PolicyAudit> audits;
        if (decision != null) {
            audits = auditService.findByDecision(PolicyDecision.valueOf(decision.toUpperCase()));
        } else {
            audits = List.of();
        }
        List<EvaluationResultDto> dtos = audits.stream()
            .map(a -> new EvaluationResultDto(
                a.requestId() != null ? a.requestId().toString() : "",
                a.decision().name(), List.of(), a.processingTimeMs(), a.success()
            )).toList();
        return ResponseEntity.ok(new EvaluationListDto(dtos, dtos.size()));
    }

    @GetMapping("/violations")
    public ResponseEntity<List<ViolationDto>> getViolations() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/reload")
    public ResponseEntity<PolicyReloadDto> reload() {
        return ResponseEntity.ok(new PolicyReloadDto(true, "Policies reloaded", System.currentTimeMillis()));
    }

    @GetMapping("/health")
    public ResponseEntity<PolicyHealthDto> health() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("service", "policy-engine");
        health.put("timestamp", System.currentTimeMillis());
        health.put("details", metricsService.getMetrics());
        return ResponseEntity.ok(new PolicyHealthDto(
            "UP", "policy-engine", System.currentTimeMillis(),
            Map.of("metrics", metricsService.getMetrics())
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PolicyErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(PolicyErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }

    private PolicyResponseDto toDto(Policy p) {
        return new PolicyResponseDto(
            p.id().toString(), p.name(), p.description(),
            p.type().name(), p.status().name(), p.scope().name(),
            p.priority(), p.module(), p.isActive(), p.metadata()
        );
    }
}
