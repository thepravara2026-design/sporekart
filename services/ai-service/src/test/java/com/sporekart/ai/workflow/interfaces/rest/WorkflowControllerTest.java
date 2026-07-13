package com.sporekart.ai.workflow.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.api.WorkflowSchedulerService;
import com.sporekart.ai.workflow.api.WorkflowService;
import com.sporekart.ai.workflow.application.WorkflowException;
import com.sporekart.ai.workflow.domain.*;
import com.sporekart.ai.workflow.infrastructure.monitoring.WorkflowMonitoringService;
import com.sporekart.ai.workflow.interfaces.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WorkflowControllerTest {

    @Mock private WorkflowService workflowService;
    @Mock private WorkflowExecutionService executionService;
    @Mock private WorkflowSchedulerService schedulerService;
    @Mock private WorkflowMonitoringService monitoringService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(WorkflowException.class)
        @ResponseBody
        public ResponseEntity<String> handleWorkflowException(WorkflowException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        var controller = new WorkflowController(workflowService, executionService, schedulerService, monitoringService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateDefinition() throws Exception {
        var def = new WorkflowDefinition(UUID.randomUUID(), "Test", "desc", "cat",
                WorkflowStatus.DRAFT, "1.0.0", WorkflowTriggerType.REST_API, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.createDefinition(anyString(), anyString(), anyString(),
                any(), anyString(), any())).thenReturn(def);

        var request = new WorkflowDefinitionRequest("Test", "desc", "cat", "REST_API", "{}", UUID.randomUUID());
        mockMvc.perform(post("/api/v1/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void shouldReturn400WhenCreatingWithInvalidTriggerType() throws Exception {
        var request = new WorkflowDefinitionRequest("Test", "desc", "cat", "INVALID_TYPE", "{}", UUID.randomUUID());
        mockMvc.perform(post("/api/v1/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetDefinition() throws Exception {
        var id = UUID.randomUUID();
        var def = new WorkflowDefinition(id, "Test", "desc", "cat",
                WorkflowStatus.DRAFT, "1.0.0", WorkflowTriggerType.REST_API, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.getDefinition(id)).thenReturn(Optional.of(def));

        mockMvc.perform(get("/api/v1/workflows/{workflowId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void shouldReturn404WhenDefinitionNotFound() throws Exception {
        var id = UUID.randomUUID();
        when(workflowService.getDefinition(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/workflows/{workflowId}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListDefinitions() throws Exception {
        when(workflowService.listDefinitions()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/workflows"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateDefinition() throws Exception {
        var id = UUID.randomUUID();
        var def = new WorkflowDefinition(id, "Updated", "desc", "cat",
                WorkflowStatus.DRAFT, "1.0.0", WorkflowTriggerType.SCHEDULED, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.updateDefinition(any(), anyString(), anyString(), anyString(),
                any(), anyString())).thenReturn(def);

        var request = new WorkflowDefinitionRequest("Updated", "desc", "cat", "SCHEDULED", "{}", UUID.randomUUID());
        mockMvc.perform(put("/api/v1/workflows/{workflowId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void shouldDeleteDefinition() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/workflows/{workflowId}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldPublishDefinition() throws Exception {
        var id = UUID.randomUUID();
        var def = new WorkflowDefinition(id, "Test", "desc", "cat",
                WorkflowStatus.ACTIVE, "1.0.0", WorkflowTriggerType.REST_API, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.publishDefinition(id)).thenReturn(def);

        mockMvc.perform(post("/api/v1/workflows/{workflowId}/publish", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldDeactivateDefinition() throws Exception {
        var id = UUID.randomUUID();
        var def = new WorkflowDefinition(id, "Test", "desc", "cat",
                WorkflowStatus.DEACTIVATED, "1.0.0", WorkflowTriggerType.REST_API, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.deactivateDefinition(id)).thenReturn(def);

        mockMvc.perform(post("/api/v1/workflows/{workflowId}/deactivate", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEACTIVATED"));
    }

    @Test
    void shouldCloneDefinition() throws Exception {
        var id = UUID.randomUUID();
        var def = new WorkflowDefinition(UUID.randomUUID(), "Cloned", "desc", "cat",
                WorkflowStatus.DRAFT, "1.0.0", WorkflowTriggerType.REST_API, "{}",
                null, false, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(workflowService.cloneDefinition(id, "Cloned")).thenReturn(def);

        mockMvc.perform(post("/api/v1/workflows/{workflowId}/clone", id)
                        .param("newName", "Cloned"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cloned"));
    }

    @Test
    void shouldAddStep() throws Exception {
        var workflowId = UUID.randomUUID();
        var step = new WorkflowStep(UUID.randomUUID(), workflowId, "Step1",
                WorkflowStepType.NOTIFICATION, 1, "{}", null, false, 5000, 3);
        when(workflowService.addStep(any(), anyString(), any(), anyInt(), anyString(),
                anyBoolean(), anyLong(), anyInt())).thenReturn(step);

        var request = new WorkflowStepRequest("Step1", "NOTIFICATION", 1, "{}", false, 5000, 3);
        mockMvc.perform(post("/api/v1/workflows/{workflowId}/steps", workflowId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Step1"));
    }

    @Test
    void shouldGetSteps() throws Exception {
        var workflowId = UUID.randomUUID();
        when(workflowService.getSteps(workflowId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/workflows/{workflowId}/steps", workflowId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRemoveStep() throws Exception {
        var workflowId = UUID.randomUUID();
        var stepId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/workflows/{workflowId}/steps/{stepId}", workflowId, stepId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldExecuteWorkflow() throws Exception {
        var workflowId = UUID.randomUUID();
        var execution = new WorkflowExecution(UUID.randomUUID(), workflowId, "1.0.0",
                WorkflowExecutionStatus.PENDING, "REST_API", "{}",
                UUID.randomUUID(), OffsetDateTime.now(), null, null, 0);
        when(executionService.startExecution(any(), any(), any())).thenReturn(execution);
        var request = new WorkflowExecutionRequest(workflowId, "REST_API", Map.of("key", "value"), UUID.randomUUID());
        mockMvc.perform(post("/api/v1/workflows/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.workflowId").value(workflowId.toString()));
    }

    @Test
    void shouldGetExecution() throws Exception {
        var executionId = UUID.randomUUID();
        var execution = new WorkflowExecution(executionId, UUID.randomUUID(), "1.0.0",
                WorkflowExecutionStatus.RUNNING, "REST_API", "{}",
                UUID.randomUUID(), OffsetDateTime.now(), null, null, 0);
        when(executionService.getExecution(executionId)).thenReturn(Optional.of(execution));

        mockMvc.perform(get("/api/v1/workflows/executions/{executionId}", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(executionId.toString()));
    }

    @Test
    void shouldReturn404WhenExecutionNotFound() throws Exception {
        var executionId = UUID.randomUUID();
        when(executionService.getExecution(executionId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/workflows/executions/{executionId}", executionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListExecutions() throws Exception {
        var workflowId = UUID.randomUUID();
        when(executionService.listExecutions(workflowId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/workflows/executions")
                        .param("workflowId", workflowId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCancelExecution() throws Exception {
        var executionId = UUID.randomUUID();
        var execution = new WorkflowExecution(executionId, UUID.randomUUID(), "1.0.0",
                WorkflowExecutionStatus.CANCELLED, "REST_API", "{}",
                UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now(), null, 0);
        when(executionService.cancelExecution(executionId)).thenReturn(execution);

        mockMvc.perform(post("/api/v1/workflows/executions/{executionId}/cancel", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldGetExecutionState() throws Exception {
        var executionId = UUID.randomUUID();
        var state = new WorkflowExecutionState(UUID.randomUUID(), executionId, UUID.randomUUID(),
                "step1", null, null, "RUNNING", OffsetDateTime.now(), OffsetDateTime.now());
        when(executionService.getExecutionState(executionId)).thenReturn(Optional.of(state));

        mockMvc.perform(get("/api/v1/workflows/executions/{executionId}/state", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStep").value("step1"));
    }

    @Test
    void shouldReturn404WhenExecutionStateNotFound() throws Exception {
        var executionId = UUID.randomUUID();
        when(executionService.getExecutionState(executionId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/workflows/executions/{executionId}/state", executionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateSchedule() throws Exception {
        var schedule = new WorkflowSchedule(UUID.randomUUID(), UUID.randomUUID(), "0 0 * * * ?",
                OffsetDateTime.now(), null, true, "UTC", null, null);
        when(schedulerService.createSchedule(any(), anyString(), any(), any(), any())).thenReturn(schedule);

        var request = new WorkflowScheduleRequest(UUID.randomUUID(), "0 0 * * * ?",
                OffsetDateTime.now(), null, "UTC");
        mockMvc.perform(post("/api/v1/workflows/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cronExpression").value("0 0 * * * ?"));
    }

    @Test
    void shouldListSchedules() throws Exception {
        var workflowId = UUID.randomUUID();
        when(schedulerService.listSchedules(workflowId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/workflows/schedules")
                        .param("workflowId", workflowId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPauseSchedule() throws Exception {
        var scheduleId = UUID.randomUUID();
        var schedule = new WorkflowSchedule(scheduleId, UUID.randomUUID(), "0 0 * * * ?",
                OffsetDateTime.now(), null, false, "UTC", null, null);
        when(schedulerService.pauseSchedule(scheduleId)).thenReturn(schedule);

        mockMvc.perform(put("/api/v1/workflows/schedules/{scheduleId}/pause", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    void shouldResumeSchedule() throws Exception {
        var scheduleId = UUID.randomUUID();
        var schedule = new WorkflowSchedule(scheduleId, UUID.randomUUID(), "0 0 * * * ?",
                OffsetDateTime.now(), null, true, "UTC", null, null);
        when(schedulerService.resumeSchedule(scheduleId)).thenReturn(schedule);

        mockMvc.perform(put("/api/v1/workflows/schedules/{scheduleId}/resume", scheduleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void shouldCheckHealth() throws Exception {
        var health = new WorkflowMonitoringService.HealthStatus("UP", 5, true);
        when(monitoringService.checkHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/workflows/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
