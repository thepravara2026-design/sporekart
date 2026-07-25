package com.sporekart.workflow.interfaces.rest;

import com.sporekart.workflow.application.service.WorkflowOrchestratorService;
import com.sporekart.workflow.domain.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflows")
public class WorkflowController {
    private final WorkflowOrchestratorService service;

    public WorkflowController(WorkflowOrchestratorService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(service.health());
    }

    // ---- Definitions ----
    @GetMapping("/definitions")
    public ResponseEntity<List<WorkflowDefinition>> getAllDefinitions() {
        return ResponseEntity.ok(service.listDefinitions());
    }

    @GetMapping("/definitions/{id}")
    public ResponseEntity<WorkflowDefinition> getDefinitionById(@PathVariable String id) {
        return service.getDefinition(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/definitions/active")
    public ResponseEntity<List<WorkflowDefinition>> getActiveDefinitions() {
        return ResponseEntity.ok(service.getActiveDefinitions());
    }

    @PostMapping("/definitions/generate")
    public ResponseEntity<List<WorkflowDefinition>> generateDefinitions() {
        return ResponseEntity.ok(service.generateDefinitions());
    }

    // ---- Instances ----
    @GetMapping("/instances")
    public ResponseEntity<List<WorkflowInstance>> getAllInstances() {
        return ResponseEntity.ok(service.listInstances());
    }

    @GetMapping("/instances/{id}")
    public ResponseEntity<WorkflowInstance> getInstanceById(@PathVariable String id) {
        return service.getInstance(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/instances/state/{state}")
    public ResponseEntity<List<WorkflowInstance>> getInstancesByState(@PathVariable WorkflowState state) {
        return ResponseEntity.ok(service.getInstancesByState(state));
    }

    // ---- Execution ----
    @PostMapping("/start")
    public ResponseEntity<WorkflowInstance> startWorkflow(@RequestBody Map<String, Object> body) {
        var definitionId = (String) body.get("definitionId");
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        var context = (Map<String, Object>) body.getOrDefault("context", Map.of());
        return ResponseEntity.ok(service.startWorkflow(definitionId, triggeredBy, context));
    }

    @PostMapping("/simulate")
    public ResponseEntity<WorkflowInstance> startSimulation(@RequestBody Map<String, Object> body) {
        var definitionId = (String) body.get("definitionId");
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        var context = (Map<String, Object>) body.getOrDefault("context", Map.of());
        return ResponseEntity.ok(service.startSimulation(definitionId, triggeredBy, context));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<WorkflowInstance> pauseWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.pauseWorkflow(id, triggeredBy));
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<WorkflowInstance> resumeWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.resumeWorkflow(id, triggeredBy));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<WorkflowInstance> cancelWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.cancelWorkflow(id, triggeredBy));
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<WorkflowInstance> retryWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.retryWorkflow(id, triggeredBy));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<WorkflowInstance> completeWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var result = (Map<String, Object>) body.getOrDefault("result", Map.of());
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.completeWorkflow(id, result, triggeredBy));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<WorkflowInstance> failWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var error = (String) body.getOrDefault("error", "Unknown error");
        var triggeredBy = (String) body.getOrDefault("triggeredBy", "system");
        return ResponseEntity.ok(service.failWorkflow(id, error, triggeredBy));
    }

    // ---- Simulation ----
    @PostMapping("/{id}/simulate")
    public ResponseEntity<WorkflowSimulation> simulateWorkflow(@PathVariable String id) {
        return ResponseEntity.ok(service.simulateWorkflow(id));
    }

    @PostMapping("/{id}/rollback")
    public ResponseEntity<WorkflowSimulation> simulateRollback(@PathVariable String id) {
        return ResponseEntity.ok(service.simulateRollback(id));
    }

    @PostMapping("/dry-run")
    public ResponseEntity<WorkflowSimulation> dryRunWorkflow(@RequestBody Map<String, Object> body) {
        var definitionId = (String) body.get("definitionId");
        var context = (Map<String, Object>) body.getOrDefault("context", Map.of());
        return ResponseEntity.ok(service.dryRunWorkflow(definitionId, context));
    }

    // ---- Audits ----
    @GetMapping("/audits")
    public ResponseEntity<List<WorkflowAudit>> getAllAudits() {
        return ResponseEntity.ok(service.getAllAudits());
    }

    @GetMapping("/audits/{instanceId}/trail")
    public ResponseEntity<List<WorkflowAudit>> getAuditTrail(@PathVariable String instanceId) {
        return ResponseEntity.ok(service.getAuditTrail(instanceId));
    }

    @GetMapping("/audits/summary")
    public ResponseEntity<Map<String, Object>> getAuditSummary() {
        return ResponseEntity.ok(service.getAuditSummary());
    }

    // ---- Queue ----
    @GetMapping("/queue")
    public ResponseEntity<List<WorkflowQueue>> getQueue() {
        return ResponseEntity.ok(service.listQueue());
    }

    @GetMapping("/queue/pending")
    public ResponseEntity<List<WorkflowQueue>> getPendingQueue() {
        return ResponseEntity.ok(service.getPendingQueue());
    }

    @GetMapping("/queue/metrics")
    public ResponseEntity<Map<String, Object>> getQueueMetrics() {
        return ResponseEntity.ok(service.getQueueMetrics());
    }

    @DeleteMapping("/queue")
    public ResponseEntity<Void> clearQueue() {
        service.clearQueue();
        return ResponseEntity.noContent().build();
    }

    // ---- Approvals ----
    @GetMapping("/approvals")
    public ResponseEntity<List<WorkflowApproval>> getApprovals() {
        return ResponseEntity.ok(service.listApprovals());
    }

    @GetMapping("/approvals/status/{status}")
    public ResponseEntity<List<WorkflowApproval>> getApprovalsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getApprovalsByStatus(status));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<WorkflowApproval> approveWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var reason = (String) body.getOrDefault("reason", "Approved");
        return ResponseEntity.ok(service.approveWorkflow(id, reason));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<WorkflowApproval> rejectWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        var reason = (String) body.getOrDefault("reason", "Rejected");
        return ResponseEntity.ok(service.rejectWorkflow(id, reason));
    }

    // ---- State Machine ----
    @GetMapping("/state-machine/transitions")
    public ResponseEntity<List<WorkflowState>> getAllowedTransitions(@RequestParam WorkflowState state) {
        return ResponseEntity.ok(service.getAllowedTransitions(state));
    }

    @GetMapping("/state-machine/validate")
    public ResponseEntity<Map<String, Object>> validateTransition(@RequestParam WorkflowState from, @RequestParam WorkflowState to) {
        var valid = service.isValidTransition(from, to);
        return ResponseEntity.ok(Map.of("valid", valid, "from", from, "to", to));
    }

    @GetMapping("/state-machine/path")
    public ResponseEntity<List<WorkflowState>> getTransitionPath(@RequestParam WorkflowState from, @RequestParam WorkflowState to) {
        return ResponseEntity.ok(service.getTransitionPath(from, to));
    }

    // ---- Cache ----
    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        return ResponseEntity.ok(service.getCacheInfo());
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        service.clearCache();
        return ResponseEntity.noContent().build();
    }

    // ---- Telemetry ----
    @GetMapping("/telemetry")
    public ResponseEntity<Map<String, Object>> getTelemetry() {
        return ResponseEntity.ok(service.getTelemetry());
    }

    @GetMapping("/telemetry/history")
    public ResponseEntity<Map<String, Object>> getTelemetryHistory() {
        return ResponseEntity.ok(service.getTelemetryHistory());
    }

    @GetMapping("/telemetry/health")
    public ResponseEntity<Map<String, Object>> getWorkflowHealth() {
        return ResponseEntity.ok(service.getWorkflowHealth());
    }
}
