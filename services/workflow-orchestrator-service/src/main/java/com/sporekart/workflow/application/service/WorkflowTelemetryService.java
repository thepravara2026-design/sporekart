package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.util.*;
import java.util.stream.Collectors;

public class WorkflowTelemetryService {
    private final WorkflowRepositoryPort repository;
    private final List<Map<String, Object>> telemetryHistory = new ArrayList<>();

    public WorkflowTelemetryService(WorkflowRepositoryPort repository) {
        this.repository = repository;
    }

    public Map<String, Object> getTelemetry() {
        var instances = repository.findAllInstances();
        var definitions = repository.findAllDefinitions();
        var simulations = repository.findAllSimulations();

        var metrics = new HashMap<String, Object>();
        metrics.put("totalDefinitions", definitions.size());
        metrics.put("totalInstances", instances.size());
        metrics.put("totalSimulations", simulations.size());
        metrics.put("totalAudits", repository.findAllAudits().size());

        metrics.put("instancesByState", instances.stream()
            .collect(Collectors.groupingBy(WorkflowInstance::state, Collectors.counting())));
        metrics.put("instancesByType", instances.stream()
            .collect(Collectors.groupingBy(WorkflowInstance::type, Collectors.counting())));
        metrics.put("definitionsByType", definitions.stream()
            .collect(Collectors.groupingBy(WorkflowDefinition::type, Collectors.counting())));

        var avgDuration = instances.stream()
            .filter(i -> i.completedAt() != null)
            .mapToLong(i -> i.completedAt().toEpochMilli() - i.createdAt().toEpochMilli())
            .average();
        metrics.put("averageExecutionDurationMs", avgDuration.orElse(0.0));

        metrics.put("health", repository.getHealthMetrics());

        telemetryHistory.add(Map.copyOf(metrics));
        return Map.copyOf(metrics);
    }

    public Map<String, Object> getWorkflowHealth() {
        var instances = repository.findAllInstances();
        var running = instances.stream().filter(i -> i.state() == WorkflowState.RUNNING).count();
        var pending = instances.stream().filter(i -> i.state() == WorkflowState.PENDING || i.state() == WorkflowState.QUEUED).count();
        var failed = instances.stream().filter(i -> i.state() == WorkflowState.FAILED).count();
        var completed = instances.stream().filter(i -> i.state() == WorkflowState.COMPLETED).count();

        var health = WorkflowHealth.HEALTHY;
        if (failed > completed && failed > 5) health = WorkflowHealth.CRITICAL;
        else if (failed > 3) health = WorkflowHealth.WARNING;

        return Map.of(
            "status", health.name(),
            "running", running,
            "pending", pending,
            "failed", failed,
            "completed", completed,
            "total", instances.size()
        );
    }

    public Map<String, Object> getTelemetryHistory() {
        return Map.of("history", List.copyOf(telemetryHistory));
    }
}
