package com.sporekart.ai.automation.interfaces.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sporekart.ai.automation.api.*;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.interfaces.rest.dto.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AutomationController.class)
class AutomationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutomationEngine automationEngine;
    @MockitoBean
    private LifecycleManager lifecycleManager;
    @MockitoBean
    private WorkflowOrchestrator workflowOrchestrator;
    @MockitoBean
    private JobExecutionService jobExecutionService;
    @MockitoBean
    private RetryManager retryManager;
    @MockitoBean
    private SchedulerService schedulerService;
    @MockitoBean
    private AutomationMetricsService automationMetricsService;
    @MockitoBean
    private AutomationKafkaEventPublisher automationKafkaEventPublisher;
    @MockitoBean
    private AutomationMonitoringService automationMonitoringService;

    @Test
    void getLifecycleDefinitionsShouldReturn200() throws Exception {
        var def = new LifecycleDefinition(UUID.randomUUID(), "test", "policy",
            LifecycleStateType.CREATED, Map.of(), Map.of(), Instant.now(), Instant.now());
        when(lifecycleManager.getAllLifecycles()).thenReturn(List.of(def));

        mockMvc.perform(get("/api/v1/governance/lifecycle"))
            .andExpect(status().isOk());
    }

    @Test
    void getWorkflowsShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/governance/workflows"))
            .andExpect(status().isOk());
    }

    @Test
    void startWorkflowShouldReturn200() throws Exception {
        var execution = new WorkflowExecution(UUID.randomUUID(), "test", WorkflowExecutionStatus.PENDING,
            Map.of(), null, 0, 3, Instant.now(), null, null);
        when(workflowOrchestrator.startWorkflow(anyString(), any())).thenReturn(execution);

        mockMvc.perform(post("/api/v1/governance/workflows")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"workflowName":"test","context":{}}
                    """))
            .andExpect(status().isAccepted());
    }

    @Test
    void createJobShouldReturn201() throws Exception {
        var job = new AutomationJob(UUID.randomUUID(), JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null);
        when(automationEngine.executeJob(any(), anyString(), any())).thenReturn(job);

        mockMvc.perform(post("/api/v1/automation/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"type":"HEALTH_CHECK","name":"test","params":{}}
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    void getJobsShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/automation/jobs"))
            .andExpect(status().isOk());
    }

    @Test
    void getJobsWithStatusShouldReturn200() throws Exception {
        when(jobExecutionService.getJobsByStatus(AutomationStatus.PENDING)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/automation/jobs?status=PENDING"))
            .andExpect(status().isOk());
    }

    @Test
    void retryJobShouldReturn200() throws Exception {
        var id = UUID.randomUUID();
        var job = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.PENDING, 1, 3, Instant.now(), null, null, null);
        when(retryManager.retryJob(id)).thenReturn(job);

        mockMvc.perform(post("/api/v1/automation/jobs/{id}/retry", id))
            .andExpect(status().isOk());
    }

    @Test
    void cancelJobShouldReturn200() throws Exception {
        var id = UUID.randomUUID();
        var job = new AutomationJob(id, JobType.HEALTH_CHECK, "test", Map.of(),
            AutomationStatus.CANCELLED, 0, 3, Instant.now(), null, Instant.now(), "Cancelled");
        when(jobExecutionService.cancelJob(id)).thenReturn(job);

        mockMvc.perform(post("/api/v1/automation/jobs/{id}/cancel", id))
            .andExpect(status().isOk());
    }

    @Test
    void getSchedulesShouldReturn200() throws Exception {
        when(schedulerService.getAllTasks()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/automation/schedules"))
            .andExpect(status().isOk());
    }

    @Test
    void getStatisticsShouldReturn200() throws Exception {
        when(automationMetricsService.getTotalWorkflows()).thenReturn(0L);
        when(automationMetricsService.getTotalJobs()).thenReturn(0L);
        when(automationMetricsService.getJobSuccessRate()).thenReturn(1.0);
        when(automationMetricsService.getRetryCount()).thenReturn(0L);
        when(automationMetricsService.getEscalationCount()).thenReturn(0L);
        when(automationMetricsService.getStatistics()).thenReturn(Map.of());

        mockMvc.perform(get("/api/v1/automation/statistics"))
            .andExpect(status().isOk());
    }

    @Test
    void healthShouldReturn200() throws Exception {
        when(automationMonitoringService.getHealthStatus()).thenReturn(Map.of());

        mockMvc.perform(get("/api/v1/automation/health"))
            .andExpect(status().isOk());
    }
}
