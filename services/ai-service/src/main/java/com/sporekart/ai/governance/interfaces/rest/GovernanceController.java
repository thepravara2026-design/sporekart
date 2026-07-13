package com.sporekart.ai.governance.interfaces.rest;

import com.sporekart.ai.governance.api.*;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.interfaces.rest.dto.*;
import com.sporekart.ai.governance.infrastructure.kafka.GovernanceKafkaEventPublisher;
import com.sporekart.ai.governance.infrastructure.monitoring.GovernanceMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/governance")
@RequiredArgsConstructor
public class GovernanceController {

    private final GovernanceEngine engine;
    private final GovernanceManager manager;
    private final GovernanceHealthService healthService;
    private final GovernanceRegistryService registryService;
    private final GovernanceKafkaEventPublisher eventPublisher;
    private final GovernanceMonitoringService monitoringService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of(
            "service", "Enterprise AI Governance",
            "version", "1.0.0",
            "endpoints", List.of(
                "/api/v1/governance",
                "/api/v1/governance/status",
                "/api/v1/governance/configuration",
                "/api/v1/governance/validate",
                "/api/v1/governance/reload",
                "/api/v1/governance/health"
            )
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<GovernanceStatusDto> getStatus() {
        Map<String, Object> status = healthService.getStatus();
        Map<String, Object> metrics = healthService.getMetrics();
        return ResponseEntity.ok(new GovernanceStatusDto(
            (boolean) status.getOrDefault("operational", false),
            (String) status.getOrDefault("mode", "DEVELOPMENT"),
            metrics
        ));
    }

    @GetMapping("/configuration")
    public ResponseEntity<List<GovernanceConfigurationDto>> getConfiguration() {
        List<GovernanceConfiguration> configs = manager.getAllConfigurations();
        return ResponseEntity.ok(configs.stream()
            .map(c -> new GovernanceConfigurationDto(
                c.key(), c.value(), c.description(),
                c.scope().name(), c.mode().name(),
                c.isActive(), c.version()
            )).toList());
    }

    @PostMapping("/validate")
    public ResponseEntity<GovernanceResponseDto> validate(@RequestBody GovernanceRequestDto request) {
        monitoringService.recordRequest();
        long start = System.currentTimeMillis();

        GovernanceRequest domainRequest = new GovernanceRequest(
            UUID.randomUUID(), request.module(), request.action(),
            request.payload() != null ? request.payload() : new HashMap<>(),
            request.metadata() != null ? request.metadata() : new HashMap<>(),
            request.userId(), request.roles() != null ? request.roles() : List.of(),
            OffsetDateTime.now()
        );

        GovernanceResponse response = engine.validate(domainRequest);

        long elapsed = System.currentTimeMillis() - start;
        monitoringService.recordValidation(elapsed);
        monitoringService.recordDecision(response.decision().name());

        eventPublisher.publishValidated(
            domainRequest.id().toString(),
            response.decision().name(),
            elapsed
        );

        return ResponseEntity.ok(new GovernanceResponseDto(
            response.requestId().toString(),
            response.decision().name(),
            response.violations().stream()
                .map(v -> new GovernanceViolationDto(
                    v.ruleName(), v.message(), v.severity().name(),
                    v.details(), v.overridable()
                )).toList(),
            response.context(),
            response.processingTimeMs()
        ));
    }

    @PostMapping("/reload")
    public ResponseEntity<GovernanceReloadDto> reload() {
        manager.reloadConfiguration();
        monitoringService.recordConfigReload();
        eventPublisher.publishReloaded(Map.of("triggeredBy", "api"));
        return ResponseEntity.ok(new GovernanceReloadDto(
            true, "Configuration reloaded successfully", System.currentTimeMillis()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<GovernanceHealthDto> health() {
        Map<String, Object> health = healthService.checkHealth();
        return ResponseEntity.ok(new GovernanceHealthDto(
            (String) health.getOrDefault("status", "DOWN"),
            (String) health.getOrDefault("service", "governance"),
            (long) health.getOrDefault("timestamp", System.currentTimeMillis()),
            (Map<String, Object>) health.getOrDefault("details", Map.of())
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GovernanceErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(GovernanceErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }
}
