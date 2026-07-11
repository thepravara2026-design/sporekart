package com.sporekart.ai.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.application.service.ERPIntegrationService;
import com.sporekart.ai.domain.model.ERPSyncLog;
import com.sporekart.ai.common.exception.ERPIntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ERPIntegrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ERPIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ERPIntegrationService erpIntegrationService;

    private ERPSyncLog syncLog;

    @BeforeEach
    void setUp() {
        syncLog = new ERPSyncLog(UUID.randomUUID(), "INVENTORY", "ERPNEXT");
        syncLog.startSync();
    }

    @Test
    void shouldConfigureERPProvider() throws Exception {
        Map<String, String> payload = Map.of(
                "provider", "ERPNEXT",
                "endpoint", "https://erpnext.example.com",
                "apiKey", "placeholder-secret-key");

        doNothing().when(erpIntegrationService).configureERPProvider("ERPNEXT", "https://erpnext.example.com",
                "placeholder-secret-key");

        mockMvc.perform(post("/erp/configure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("ERP provider configured"));
    }

    @Test
    void shouldInitiateSync() throws Exception {
        Map<String, String> payload = Map.of("syncType", "INVENTORY", "provider", "ERPNEXT");

        when(erpIntegrationService.initiateSync("INVENTORY", "ERPNEXT")).thenReturn(syncLog);

        mockMvc.perform(post("/erp/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.syncId").value(syncLog.getId().toString()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldReturnBadRequestWhenSyncTypeUnsupported() throws Exception {
        Map<String, String> payload = Map.of("syncType", "UNKNOWN", "provider", "SAP");
        when(erpIntegrationService.initiateSync("UNKNOWN", "SAP"))
                .thenThrow(new ERPIntegrationException("Unsupported ERP sync type: UNKNOWN"));

        mockMvc.perform(post("/erp/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Unsupported ERP sync type: UNKNOWN"));
    }

    @Test
    void shouldInitiateSalesSync() throws Exception {
        ERPSyncLog salesLog = new ERPSyncLog(UUID.randomUUID(), "SALES", "SAP");
        salesLog.startSync();
        Map<String, String> payload = Map.of("syncType", "SALES", "provider", "SAP");

        when(erpIntegrationService.initiateSync("SALES", "SAP")).thenReturn(salesLog);

        mockMvc.perform(post("/erp/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.syncId").value(salesLog.getId().toString()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldCompleteSync() throws Exception {
        doNothing().when(erpIntegrationService).completeSyncLog(eq(syncLog.getId()), eq(120), eq(3));

        mockMvc.perform(post("/erp/sync/" + syncLog.getId() + "/complete")
                .param("synced", "120")
                .param("failed", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Sync completed"));
    }

    @Test
    void shouldFailSync() throws Exception {
        doNothing().when(erpIntegrationService).failSyncLog(eq(syncLog.getId()), eq("timeout"));

        mockMvc.perform(post("/erp/sync/" + syncLog.getId() + "/fail")
                .param("error", "timeout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Sync failed"));
    }

    @Test
    void shouldReturnERPStatus() throws Exception {
        Map<String, Object> details = Map.of("provider", "ERPNEXT", "isConfigured", true);
        when(erpIntegrationService.getERPStatus("ERPNEXT")).thenReturn(details);

        mockMvc.perform(get("/erp/status").param("provider", "ERPNEXT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details.provider").value("ERPNEXT"))
                .andExpect(jsonPath("$.details.isConfigured").value(true));
    }

    @Test
    void shouldReturnSyncHistory() throws Exception {
        com.sporekart.ai.interfaces.rest.dto.ERPSyncHistoryEntry historyEntry =
                new com.sporekart.ai.interfaces.rest.dto.ERPSyncHistoryEntry(
                        syncLog.getId(), syncLog.getSyncType(), syncLog.getErpProvider(),
                        syncLog.getSyncStatus(), syncLog.getRecordsSynced(), syncLog.getRecordsFailed(),
                        syncLog.getSyncStartTime(), syncLog.getSyncEndTime(), syncLog.getErrorMessage());

        when(erpIntegrationService.getSyncHistory("ERPNEXT")).thenReturn(List.of(historyEntry));

        mockMvc.perform(get("/erp/sync-history").param("provider", "ERPNEXT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.history[0].erpProvider").value("ERPNEXT"));
    }

    @Test
    void shouldReturnBadRequestWhenConfigureProviderUnknown() throws Exception {
        Map<String, String> payload = Map.of("provider", "UNKNOWN", "endpoint", "https://invalid", "apiKey", "key");
        Mockito.doThrow(new ERPIntegrationException("Unknown ERP provider: UNKNOWN"))
                .when(erpIntegrationService).configureERPProvider("UNKNOWN", "https://invalid", "key");

        mockMvc.perform(post("/erp/configure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Unknown ERP provider: UNKNOWN"));
    }

    @Test
    void shouldReturnBadRequestWhenSyncInitiationFails() throws Exception {
        Map<String, String> payload = Map.of("syncType", "INVENTORY", "provider", "ERPNEXT");
        when(erpIntegrationService.initiateSync("INVENTORY", "ERPNEXT"))
                .thenThrow(new ERPIntegrationException("ERP sync failed"));

        mockMvc.perform(post("/erp/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("ERP sync failed"));
    }

    @Test
    void shouldReturnBadRequestWhenCompleteSyncLogUnknown() throws Exception {
        UUID unknownId = UUID.randomUUID();
        Mockito.doThrow(new ERPIntegrationException("Sync log not found"))
                .when(erpIntegrationService).completeSyncLog(eq(unknownId), eq(0), eq(0));

        mockMvc.perform(post("/erp/sync/" + unknownId + "/complete")
                .param("synced", "0")
                .param("failed", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Sync log not found"));
    }

    @Test
    void shouldReturnBadRequestWhenFailSyncLogUnknown() throws Exception {
        UUID unknownId = UUID.randomUUID();
        Mockito.doThrow(new ERPIntegrationException("Sync log not found"))
                .when(erpIntegrationService).failSyncLog(eq(unknownId), eq("error"));

        mockMvc.perform(post("/erp/sync/" + unknownId + "/fail")
                .param("error", "error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Sync log not found"));
    }
}
