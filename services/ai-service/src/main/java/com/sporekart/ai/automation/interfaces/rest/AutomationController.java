package com.sporekart.ai.automation.interfaces.rest;

import com.sporekart.ai.automation.api.AutomationEngine;
import com.sporekart.ai.automation.api.AutomationKafkaEventPublisher;
import com.sporekart.ai.automation.api.AutomationMetricsService;
import com.sporekart.ai.automation.api.AutomationMonitoringService;
import com.sporekart.ai.automation.api.JobExecutionService;
import com.sporekart.ai.automation.api.LifecycleManager;
import com.sporekart.ai.automation.api.RetryManager;
import com.sporekart.ai.automation.api.SchedulerService;
import com.sporekart.ai.automation.api.WorkflowOrchestrator;
import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.AutomationStatus;
import com.sporekart.ai.automation.domain.JobType;
import com.sporekart.ai.automation.domain.LifecycleDefinition;
import com.sporekart.ai.automation.domain.LifecycleState;
import com.sporekart.ai.automation.domain.LifecycleStateType;
import com.sporekart.ai.automation.domain.ScheduledTask;
import com.sporekart.ai.automation.domain.WorkflowExecution;
import com.sporekart.ai.automation.interfaces.rest.dto.ErrorDto;
import com.sporekart.ai.automation.interfaces.rest.dto.HealthDto;
import com.sporekart.ai.automation.interfaces.rest.dto.JobRequestDto;
import com.sporekart.ai.automation.interfaces.rest.dto.JobResponseDto;
import com.sporekart.ai.automation.interfaces.rest.dto.LifecycleDto;
import com.sporekart.ai.automation.interfaces.rest.dto.ScheduleDto;
import com.sporekart.ai.automation.interfaces.rest.dto.StatsDto;
import com.sporekart.ai.automation.interfaces.rest.dto.WorkflowRequestDto;
import com.sporekart.ai.automation.interfaces.rest.dto.WorkflowResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AutomationController {

    private final AutomationEngine automationEngine;
    private final LifecycleManager lifecycleManager;
    private final WorkflowOrchestrator workflowOrchestrator;
    private final JobExecutionService jobExecutionService;
    private final RetryManager retryManager;
    private final SchedulerService schedulerService;
    private final AutomationMetricsService automationMetricsService;
    private final AutomationKafkaEventPublisher automationKafkaEventPublisher;
    private final AutomationMonitoringService automationMonitoringService;

    public AutomationController(AutomationEngine automationEngine,
                                LifecycleManager lifecycleManager,
                                WorkflowOrchestrator workflowOrchestrator,
                                JobExecutionService jobExecutionService,
                                RetryManager retryManager,
                                SchedulerService schedulerService,
                                AutomationMetricsService automationMetricsService,
                                AutomationKafkaEventPublisher automationKafkaEventPublisher,
                                AutomationMonitoringService automationMonitoringService) {
        this.automationEngine = automationEngine;
        this.lifecycleManager = lifecycleManager;
        this.workflowOrchestrator = workflowOrchestrator;
        this.jobExecutionService = jobExecutionService;
        this.retryManager = retryManager;
        this.schedulerService = schedulerService;
        this.automationMetricsService = automationMetricsService;
        this.automationKafkaEventPublisher = automationKafkaEventPublisher;
        this.automationMonitoringService = automationMonitoringService;
    }

    @GetMapping("/api/v1/governance/lifecycle")
    public ResponseEntity<List<LifecycleDto>> getLifecycleDefinitions() {
        var defs = lifecycleManager.getAllLifecycles();
        var dtos = defs.stream().map(this::toLifecycleDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/v1/governance/workflows")
    public ResponseEntity<List<WorkflowResponseDto>> getWorkflows() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/api/v1/governance/workflows")
    public ResponseEntity<WorkflowResponseDto> startWorkflow(@RequestBody WorkflowRequestDto request) {
        var execution = workflowOrchestrator.startWorkflow(request.workflowName(), request.context());
        automationKafkaEventPublisher.publishWorkflowStarted(execution);
        automationMonitoringService.recordWorkflowStarted();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(toWorkflowResponseDto(execution));
    }

    @PostMapping("/api/v1/automation/jobs")
    public ResponseEntity<JobResponseDto> createJob(@RequestBody JobRequestDto request) {
        var type = JobType.valueOf(request.type().toUpperCase());
        var job = automationEngine.executeJob(type, request.name(), request.params());
        automationKafkaEventPublisher.publishJobCreated(job);
        automationMonitoringService.recordJobCreated();
        return ResponseEntity.status(HttpStatus.CREATED).body(toJobResponseDto(job));
    }

    @GetMapping("/api/v1/automation/jobs")
    public ResponseEntity<List<JobResponseDto>> getJobs(@RequestParam(required = false) String status) {
        List<AutomationJob> jobs;
        if (status != null && !status.isBlank()) {
            var automationStatus = AutomationStatus.valueOf(status.toUpperCase());
            jobs = jobExecutionService.getJobsByStatus(automationStatus);
        } else {
            jobs = List.of();
        }
        var dtos = jobs.stream().map(this::toJobResponseDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/v1/automation/jobs/{id}/retry")
    public ResponseEntity<JobResponseDto> retryJob(@PathVariable UUID id) {
        var job = retryManager.retryJob(id);
        automationKafkaEventPublisher.publishJobUpdated(job);
        return ResponseEntity.ok(toJobResponseDto(job));
    }

    @PostMapping("/api/v1/automation/jobs/{id}/cancel")
    public ResponseEntity<JobResponseDto> cancelJob(@PathVariable UUID id,
                                                    @RequestParam(required = false) String reason) {
        var job = jobExecutionService.cancelJob(id);
        automationKafkaEventPublisher.publishJobUpdated(job);
        return ResponseEntity.ok(toJobResponseDto(job));
    }

    @GetMapping("/api/v1/automation/schedules")
    public ResponseEntity<List<ScheduleDto>> getSchedules() {
        var tasks = schedulerService.getAllTasks();
        var dtos = tasks.stream().map(this::toScheduleDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/v1/automation/statistics")
    public ResponseEntity<StatsDto> getStatistics() {
        var stats = new StatsDto(
            automationMetricsService.getTotalWorkflows(),
            automationMetricsService.getTotalJobs(),
            automationMetricsService.getJobSuccessRate(),
            automationMetricsService.getRetryCount(),
            automationMetricsService.getEscalationCount(),
            automationMetricsService.getStatistics()
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/v1/automation/health")
    public ResponseEntity<HealthDto> health() {
        var health = new HealthDto(
            "UP",
            "automation-service",
            Instant.now().toEpochMilli(),
            automationMonitoringService.getHealthStatus()
        );
        return ResponseEntity.ok(health);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        var error = ErrorDto.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(IllegalArgumentException ex) {
        var error = ErrorDto.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private LifecycleDto toLifecycleDto(LifecycleDefinition def) {
        return new LifecycleDto(
            def.id().toString(),
            def.name(),
            def.entityType(),
            def.initialState().name(),
            def.transitions().entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().name(),
                    e -> e.getValue().entrySet().stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().name()
                        ))
                )),
            def.config()
        );
    }

    private WorkflowResponseDto toWorkflowResponseDto(WorkflowExecution exec) {
        return new WorkflowResponseDto(
            exec.id().toString(),
            exec.workflowName(),
            exec.status().name(),
            exec.result(),
            exec.startedAt() != null ? exec.startedAt().toString() : null,
            exec.completedAt() != null ? exec.completedAt().toString() : null
        );
    }

    private JobResponseDto toJobResponseDto(AutomationJob job) {
        return new JobResponseDto(
            job.id().toString(),
            job.type().name(),
            job.name(),
            job.status().name(),
            job.retryCount(),
            job.maxRetries(),
            job.scheduledAt() != null ? job.scheduledAt().toString() : null
        );
    }

    private ScheduleDto toScheduleDto(ScheduledTask task) {
        return new ScheduleDto(
            task.id().toString(),
            task.name(),
            task.jobType().name(),
            task.frequency().name(),
            task.cronExpression(),
            task.active(),
            task.nextRunAt() != null ? task.nextRunAt().toString() : null
        );
    }
}
