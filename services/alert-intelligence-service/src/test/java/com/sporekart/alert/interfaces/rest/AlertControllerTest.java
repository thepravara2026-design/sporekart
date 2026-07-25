package com.sporekart.alert.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void healthShouldReturnUp() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void getAllAlertsShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/v1/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAlertByIdShouldReturn404ForMissing() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/NONEXISTENT"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAlertsBySeverityShouldFilter() throws Exception {
        mockMvc.perform(get("/api/v1/alerts").param("severity", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAlertsByCategoryShouldFilter() throws Exception {
        mockMvc.perform(get("/api/v1/alerts").param("category", "SECURITY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAlertsByDomainShouldFilter() throws Exception {
        mockMvc.perform(get("/api/v1/alerts").param("domain", "PLATFORM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAlertsByStatusShouldFilter() throws Exception {
        mockMvc.perform(get("/api/v1/alerts").param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void generateAlertsShouldReturnAlerts() throws Exception {
        mockMvc.perform(post("/api/v1/alerts/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(9))));
    }

    @Test
    void acknowledgeAlertShouldReturnUpdatedAlert() throws Exception {
        var result = mockMvc.perform(post("/api/v1/alerts/generate"))
                .andExpect(status().isOk())
                .andReturn();
        var json = result.getResponse().getContentAsString();
        var id = objectMapper.readTree(json).get(0).get("id").asText();

        mockMvc.perform(post("/api/v1/alerts/{id}/acknowledge", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACKNOWLEDGED"));
    }

    @Test
    void resolveAlertShouldReturnUpdatedAlert() throws Exception {
        var result = mockMvc.perform(post("/api/v1/alerts/generate"))
                .andExpect(status().isOk())
                .andReturn();
        var json = result.getResponse().getContentAsString();
        var id = objectMapper.readTree(json).get(0).get("id").asText();

        mockMvc.perform(post("/api/v1/alerts/{id}/resolve", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void getAlertHistoryShouldReturnCounts() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("totalAlerts")));
    }

    @Test
    void getTelemetryShouldReturnMetrics() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/telemetry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("alertCount")));
    }

    @Test
    void cacheEndpointsShouldWork() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/cache"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("enabled")));

        mockMvc.perform(delete("/api/v1/alerts/cache"))
                .andExpect(status().isNoContent());
    }

    @Test
    void generateAlertsForCategoryShouldReturnFiltered() throws Exception {
        mockMvc.perform(post("/api/v1/alerts/generate/SECURITY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void risksEndpointsShouldWork() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/risks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/v1/alerts/risks/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("totalRisks")));

        mockMvc.perform(post("/api/v1/alerts/risks/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))));
    }

    @Test
    void anomaliesEndpointsShouldWork() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/anomalies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(post("/api/v1/alerts/anomalies/detect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(8))));
    }

    @Test
    void timelineEndpointsShouldWork() throws Exception {
        mockMvc.perform(get("/api/v1/alerts/timeline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(post("/api/v1/alerts/timeline/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(9))));
    }
}
