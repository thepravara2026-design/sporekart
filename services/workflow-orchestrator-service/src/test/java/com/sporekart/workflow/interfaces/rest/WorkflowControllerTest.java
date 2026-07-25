package com.sporekart.workflow.interfaces.rest;

import com.sporekart.workflow.application.service.WorkflowOrchestratorService;
import com.sporekart.workflow.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkflowOrchestratorService service;

    @Test
    void shouldReturnHealth() throws Exception {
        when(service.health()).thenReturn(Map.of("status", "UP", "service", "workflow-orchestrator-service"));

        mockMvc.perform(get("/workflows/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldReturnAllDefinitions() throws Exception {
        var def = WorkflowDefinition.create("Test", "Desc", WorkflowType.ORDER, "Domain", "owner", "1.0", List.of(), Map.of(), Map.of());
        when(service.listDefinitions()).thenReturn(List.of(def));

        mockMvc.perform(get("/workflows/definitions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void shouldReturnDefinitionById() throws Exception {
        var def = WorkflowDefinition.create("Test", "Desc", WorkflowType.ORDER, "Domain", "owner", "1.0", List.of(), Map.of(), Map.of());
        when(service.getDefinition(def.id())).thenReturn(Optional.of(def));

        mockMvc.perform(get("/workflows/definitions/" + def.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void shouldReturn404ForMissingDefinition() throws Exception {
        when(service.getDefinition("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/workflows/definitions/unknown"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldGenerateDefinitions() throws Exception {
        var defs = List.of(WorkflowDefinition.create("G1", "", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of()));
        when(service.generateDefinitions()).thenReturn(defs);

        mockMvc.perform(post("/workflows/definitions/generate"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAllInstances() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        when(service.listInstances()).thenReturn(List.of(instance));

        mockMvc.perform(get("/workflows/instances"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void shouldReturnInstanceById() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        when(service.getInstance(instance.id())).thenReturn(Optional.of(instance));

        mockMvc.perform(get("/workflows/instances/" + instance.id()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnInstancesByState() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        when(service.getInstancesByState(WorkflowState.RUNNING)).thenReturn(List.of(instance));

        mockMvc.perform(get("/workflows/instances/state/RUNNING"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldStartWorkflow() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        when(service.startWorkflow(anyString(), anyString(), any())).thenReturn(instance);

        mockMvc.perform(post("/workflows/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"definitionId\":\"def-1\",\"triggeredBy\":\"user\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldStartSimulation() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", true, false, 3);
        when(service.startSimulation(anyString(), anyString(), any())).thenReturn(instance);

        mockMvc.perform(post("/workflows/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"definitionId\":\"def-1\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldPauseWorkflow() throws Exception {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        when(service.pauseWorkflow(anyString(), anyString())).thenReturn(instance);

        mockMvc.perform(post("/workflows/inst-1/pause")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldResumeWorkflow() throws Exception {
        when(service.resumeWorkflow(anyString(), anyString())).thenReturn(
            WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3));

        mockMvc.perform(post("/workflows/inst-1/resume")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCancelWorkflow() throws Exception {
        when(service.cancelWorkflow(anyString(), anyString())).thenReturn(
            WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3));

        mockMvc.perform(post("/workflows/inst-1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldRetryWorkflow() throws Exception {
        when(service.retryWorkflow(anyString(), anyString())).thenReturn(
            WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3));

        mockMvc.perform(post("/workflows/inst-1/retry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldSimulateWorkflow() throws Exception {
        when(service.simulateWorkflow(anyString())).thenReturn(
            WorkflowSimulation.create("inst-1", "Test", WorkflowType.ORDER, true, false, true, List.of(), List.of(), List.of(), Map.of()));

        mockMvc.perform(post("/workflows/inst-1/simulate"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldSimulateRollback() throws Exception {
        when(service.simulateRollback(anyString())).thenReturn(
            WorkflowSimulation.create("inst-1", "Test", WorkflowType.ORDER, true, false, true, List.of(), List.of(), List.of(), Map.of()));

        mockMvc.perform(post("/workflows/inst-1/rollback"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldDryRun() throws Exception {
        when(service.dryRunWorkflow(anyString(), any())).thenReturn(
            WorkflowSimulation.create("dry-run", "Test", WorkflowType.ORDER, true, false, true, List.of(), List.of(), List.of(), Map.of()));

        mockMvc.perform(post("/workflows/dry-run")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"definitionId\":\"def-1\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAudits() throws Exception {
        var audit = WorkflowAudit.create("inst-1", "TEST", "user", "test", "SUCCESS", Map.of());
        when(service.getAllAudits()).thenReturn(List.of(audit));

        mockMvc.perform(get("/workflows/audits"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnQueue() throws Exception {
        when(service.listQueue()).thenReturn(List.of(
            WorkflowQueue.create("inst-1", "Test", WorkflowType.ORDER, 5, "STANDARD", Map.of())));

        mockMvc.perform(get("/workflows/queue"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnTelemetry() throws Exception {
        when(service.getTelemetry()).thenReturn(Map.of("totalDefinitions", 10));

        mockMvc.perform(get("/workflows/telemetry"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalDefinitions").value(10));
    }

    @Test
    void shouldReturnWorkflowHealth() throws Exception {
        when(service.getWorkflowHealth()).thenReturn(Map.of("status", "HEALTHY"));

        mockMvc.perform(get("/workflows/telemetry/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("HEALTHY"));
    }

    @Test
    void shouldClearCache() throws Exception {
        mockMvc.perform(delete("/workflows/cache"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldClearQueue() throws Exception {
        mockMvc.perform(delete("/workflows/queue"))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldCompleteWorkflow() throws Exception {
        when(service.completeWorkflow(anyString(), any(), anyString())).thenReturn(
            WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3));

        mockMvc.perform(post("/workflows/inst-1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"result\":{\"status\":\"success\"}}"))
            .andExpect(status().isOk());
    }
}
