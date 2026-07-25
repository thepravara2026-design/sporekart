package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.WorkflowTelemetryService;

import java.util.Map;

public class WorkflowTelemetry {
    private final WorkflowTelemetryService telemetryService;

    public WorkflowTelemetry(WorkflowTelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    public Map<String, Object> getMetrics() {
        return telemetryService.getTelemetry();
    }

    public Map<String, Object> getHealth() {
        return telemetryService.getWorkflowHealth();
    }

    public Map<String, Object> getHistory() {
        return telemetryService.getTelemetryHistory();
    }
}
