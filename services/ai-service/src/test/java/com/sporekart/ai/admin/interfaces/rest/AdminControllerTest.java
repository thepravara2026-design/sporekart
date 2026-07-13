package com.sporekart.ai.admin.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.interfaces.rest.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private Object configurationManager;
    @MockitoBean private com.sporekart.ai.core.application.featureflag.FeatureFlagService featureFlagService;
    @MockitoBean private Object environmentManager;
    @MockitoBean private Object configurationVersionManager;
    @MockitoBean private Object configurationSnapshotService;
    @MockitoBean private Object configurationValidationService;
    @MockitoBean private Object administrationService;
    @MockitoBean private Object administrationAuditService;
    @MockitoBean private Object administrationMetricsService;
    @MockitoBean private Object adminKafkaEventPublisher;
    @MockitoBean private Object adminMonitoringService;

    @Test
    void testGetConfiguration() throws Exception {
        mockMvc.perform(get("/api/v1/admin/configuration")
                        .param("key", "cfg1")
                        .param("module", "governance")
                        .param("environment", "production"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cfg-1"))
                .andExpect(jsonPath("$.key").value("cfg1"));
    }

    @Test
    void testUpdateConfiguration() throws Exception {
        var dto = new ConfigUpdateDto("key1", "value1", "module1", "env1", "desc");
        mockMvc.perform(put("/api/v1/admin/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cfg-1"))
                .andExpect(jsonPath("$.key").value("key1"))
                .andExpect(jsonPath("$.value").value("value1"));
    }

    @Test
    void testGetFeatureFlags() throws Exception {
        mockMvc.perform(get("/api/v1/admin/feature-flags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testUpdateFeatureFlag() throws Exception {
        var dto = new FeatureFlagUpdateDto("ff-key", true, "production", "governance", Map.of());
        mockMvc.perform(put("/api/v1/admin/feature-flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("ff-key"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void testGetModules() throws Exception {
        mockMvc.perform(get("/api/v1/admin/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testUpdateModule() throws Exception {
        var dto = new ModuleUpdateDto("governance", true);
        mockMvc.perform(put("/api/v1/admin/modules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("governance"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void testExportConfiguration() throws Exception {
        mockMvc.perform(post("/api/v1/admin/configuration/export")
                        .param("environment", "production"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.environment").value("production"));
    }

    @Test
    void testImportConfiguration() throws Exception {
        var dto = new ImportDto(Map.of("key1", "value1"), "production", false);
        mockMvc.perform(post("/api/v1/admin/configuration/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testRollbackConfiguration() throws Exception {
        var configId = UUID.randomUUID();
        var dto = new RollbackDto(configId, 1, "rollback reason");
        mockMvc.perform(post("/api/v1/admin/configuration/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(configId.toString()))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void testGetAuditLogs() throws Exception {
        mockMvc.perform(get("/api/v1/admin/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/v1/admin/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("admin-service"));
    }
}
