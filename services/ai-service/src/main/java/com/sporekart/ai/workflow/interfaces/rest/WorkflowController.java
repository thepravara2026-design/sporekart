package com.sporekart.ai.workflow.interfaces.rest;

import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.api.WorkflowSchedulerService;
import com.sporekart.ai.workflow.api.WorkflowService;
import com.sporekart.ai.workflow.application.WorkflowException;
import com.sporekart.ai.workflow.domain.WorkflowTriggerType;
import com.sporekart.ai.workflow.infrastructure.monitoring.WorkflowMonitoringService;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowDefinitionRequest;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowDefinitionResponse;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowExecutionRequest;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowExecutionResponse;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowExecutionStateResponse;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowScheduleRequest;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowScheduleResponse;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowStepRequest;
import com.sporekart.ai.workflow.interfaces.rest.dto.WorkflowStepResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@Tag(name = "Workflow API", description = "Enterprise AI workflow management endpoints")
public class WorkflowController {

    private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

    private final WorkflowService workflowService;
    private final WorkflowExecutionService executionService;
    private final WorkflowSchedulerService schedulerService;
    private final WorkflowMonitoringService monitoringService;

    public WorkflowController(WorkflowService workflowService,
                              WorkflowExecutionService executionService,
                              WorkflowSchedulerService schedulerService,
                              WorkflowMonitoringService monitoringService) {
        this.workflowService = workflowService;
        this.executionService = executionService;
        this.schedulerService = schedulerService;
        this.monitoringService = monitoringService;
    }

    @PostMapping
    @Operation(summary = "Create a new workflow definition")
    public ResponseEntity<WorkflowDefinitionResponse> createDefinition(
            @Valid @RequestBody WorkflowDefinitionRequest request) {
        var triggerType = WorkflowTriggerType.valueOf(request.triggerType().toUpperCase());
        var def = workflowService.createDefinition(
                request.name(), request.description(), request.category(),
                triggerType, request.triggerConfig(), request.createdBy());
        log.info("Created workflow definition: {}", def.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkflowDefinitionResponse.from(def));
    }

    @GetMapping("/{workflowId}")
    @Operation(summary = "Get a workflow definition by ID")
    public ResponseEntity<WorkflowDefinitionResponse> getDefinition(@PathVariable UUID workflowId) {
        var def = workflowService.getDefinition(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + workflowId));
        return ResponseEntity.ok(WorkflowDefinitionResponse.from(def));
    }

    @GetMapping
    @Operation(summary = "List all workflow definitions")
    public ResponseEntity<List<WorkflowDefinitionResponse>> listDefinitions() {
        var defs = workflowService.listDefinitions();
        var response = defs.stream().map(WorkflowDefinitionResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{workflowId}")
    @Operation(summary = "Update a workflow definition")
    public ResponseEntity<WorkflowDefinitionResponse> updateDefinition(
            @PathVariable UUID workflowId,
            @Valid @RequestBody WorkflowDefinitionRequest request) {
        var triggerType = WorkflowTriggerType.valueOf(request.triggerType().toUpperCase());
        var def = workflowService.updateDefinition(
                workflowId, request.name(), request.description(),
                request.category(), triggerType, request.triggerConfig());
        return ResponseEntity.ok(WorkflowDefinitionResponse.from(def));
    }

    @DeleteMapping("/{workflowId}")
    @Operation(summary = "Delete a workflow definition")
    public ResponseEntity<Void> deleteDefinition(@PathVariable UUID workflowId) {
        workflowService.deleteDefinition(workflowId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workflowId}/publish")
    @Operation(summary = "Publish a workflow definition (DRAFT -> ACTIVE)")
    public ResponseEntity<WorkflowDefinitionResponse> publishDefinition(@PathVariable UUID workflowId) {
        var def = workflowService.publishDefinition(workflowId);
        return ResponseEntity.ok(WorkflowDefinitionResponse.from(def));
    }

    @PostMapping("/{workflowId}/deactivate")
    @Operation(summary = "Deactivate a workflow definition (ACTIVE -> INACTIVE)")
    public ResponseEntity<WorkflowDefinitionResponse> deactivateDefinition(@PathVariable UUID workflowId) {
        var def = workflowService.deactivateDefinition(workflowId);
        return ResponseEntity.ok(WorkflowDefinitionResponse.from(def));
    }

    @PostMapping("/{workflowId}/clone")
    @Operation(summary = "Clone a workflow definition")
    public ResponseEntity<WorkflowDefinitionResponse> cloneDefinition(
            @PathVariable UUID workflowId,
            @RequestParam String newName) {
        var def = workflowService.cloneDefinition(workflowId, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkflowDefinitionResponse.from(def));
    }

    @PostMapping("/{workflowId}/steps")
    @Operation(summary = "Add a step to a workflow definition")
    public ResponseEntity<WorkflowStepResponse> addStep(
            @PathVariable UUID workflowId,
            @Valid @RequestBody WorkflowStepRequest request) {
        var stepType = com.sporekart.ai.workflow.domain.WorkflowStepType.valueOf(request.stepType().toUpperCase());
        var step = workflowService.addStep(workflowId, request.name(), stepType,
                request.orderIndex(), request.config(), request.isOptional(),
                request.timeoutMs(), request.maxRetries());
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkflowStepResponse.from(step));
    }

    @GetMapping("/{workflowId}/steps")
    @Operation(summary = "Get steps for a workflow definition")
    public ResponseEntity<List<WorkflowStepResponse>> getSteps(@PathVariable UUID workflowId) {
        var steps = workflowService.getSteps(workflowId);
        var response = steps.stream().map(WorkflowStepResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{workflowId}/steps/{stepId}")
    @Operation(summary = "Remove a step from a workflow definition")
    public ResponseEntity<Void> removeStep(
            @PathVariable UUID workflowId,
            @PathVariable UUID stepId) {
        workflowService.removeStep(workflowId, stepId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute a workflow")
    public ResponseEntity<WorkflowExecutionResponse> executeWorkflow(
            @Valid @RequestBody WorkflowExecutionRequest request) {
        var execution = executionService.startExecution(
                request.workflowId(), request.inputData(), request.startedBy());
        log.info("Started workflow execution: {}", execution.id());
        monitoringService.recordExecutionStarted();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(WorkflowExecutionResponse.from(execution));
    }

    @GetMapping("/executions/{executionId}")
    @Operation(summary = "Get workflow execution status")
    public ResponseEntity<WorkflowExecutionResponse> getExecution(@PathVariable UUID executionId) {
        var execution = executionService.getExecution(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
        return ResponseEntity.ok(WorkflowExecutionResponse.from(execution));
    }

    @GetMapping("/executions")
    @Operation(summary = "List executions for a workflow")
    public ResponseEntity<List<WorkflowExecutionResponse>> listExecutions(
            @RequestParam UUID workflowId) {
        var executions = executionService.listExecutions(workflowId);
        var response = executions.stream().map(WorkflowExecutionResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/executions/{executionId}/cancel")
    @Operation(summary = "Cancel a workflow execution")
    public ResponseEntity<WorkflowExecutionResponse> cancelExecution(@PathVariable UUID executionId) {
        var execution = executionService.cancelExecution(executionId);
        log.info("Cancelled workflow execution: {}", executionId);
        return ResponseEntity.ok(WorkflowExecutionResponse.from(execution));
    }

    @GetMapping("/executions/{executionId}/state")
    @Operation(summary = "Get execution state machine state")
    public ResponseEntity<WorkflowExecutionStateResponse> getExecutionState(@PathVariable UUID executionId) {
        var state = executionService.getExecutionState(executionId)
                .orElseThrow(() -> new WorkflowException("Execution state not found: " + executionId));
        return ResponseEntity.ok(WorkflowExecutionStateResponse.from(state));
    }

    @PostMapping("/schedules")
    @Operation(summary = "Create a workflow schedule")
    public ResponseEntity<WorkflowScheduleResponse> createSchedule(
            @Valid @RequestBody WorkflowScheduleRequest request) {
        var schedule = schedulerService.createSchedule(
                request.workflowId(), request.cronExpression(),
                request.startAt(), request.endAt(), request.timezone());
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkflowScheduleResponse.from(schedule));
    }

    @GetMapping("/schedules")
    @Operation(summary = "List schedules for a workflow")
    public ResponseEntity<List<WorkflowScheduleResponse>> listSchedules(
            @RequestParam UUID workflowId) {
        var schedules = schedulerService.listSchedules(workflowId);
        var response = schedules.stream().map(WorkflowScheduleResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{scheduleId}/pause")
    @Operation(summary = "Pause a workflow schedule")
    public ResponseEntity<WorkflowScheduleResponse> pauseSchedule(@PathVariable UUID scheduleId) {
        var schedule = schedulerService.pauseSchedule(scheduleId);
        return ResponseEntity.ok(WorkflowScheduleResponse.from(schedule));
    }

    @PutMapping("/schedules/{scheduleId}/resume")
    @Operation(summary = "Resume a paused workflow schedule")
    public ResponseEntity<WorkflowScheduleResponse> resumeSchedule(@PathVariable UUID scheduleId) {
        var schedule = schedulerService.resumeSchedule(scheduleId);
        return ResponseEntity.ok(WorkflowScheduleResponse.from(schedule));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check for workflow module")
    public ResponseEntity<WorkflowMonitoringService.HealthStatus> health() {
        return ResponseEntity.ok(monitoringService.checkHealth());
    }
}
