package com.sporekart.report.interfaces.rest;

import com.sporekart.report.application.service.ReportingService;
import com.sporekart.report.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.sporekart.report.config.SecurityConfig;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@Import(SecurityConfig.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingService service;

    @Test
    @WithMockUser
    void shouldReturnHealth() throws Exception {
        when(service.health()).thenReturn(Map.of("status", "UP", "service", "reporting-service"));
        mockMvc.perform(get("/api/v1/reports/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @WithMockUser
    void shouldReturnAllReports() throws Exception {
        when(service.listReports()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reports"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnReportById() throws Exception {
        String id = UUID.randomUUID().toString();
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        when(service.getReport(id)).thenReturn(Optional.of(report));
        mockMvc.perform(get("/api/v1/reports/" + id))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturn404ForMissingReport() throws Exception {
        when(service.getReport("missing")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/reports/missing"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldGenerateAllReports() throws Exception {
        when(service.generateAllReports()).thenReturn(List.of());
        mockMvc.perform(post("/api/v1/reports/generate"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldGenerateReportsForCategory() throws Exception {
        when(service.generateReportsForCategory(ReportCategory.EXECUTIVE)).thenReturn(List.of());
        mockMvc.perform(post("/api/v1/reports/generate/EXECUTIVE"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnTemplates() throws Exception {
        when(service.listTemplates()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reports/templates"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnTemplateById() throws Exception {
        String id = UUID.randomUUID().toString();
        ReportTemplate t = ReportTemplate.create("T", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), Map.of());
        when(service.getTemplate(id)).thenReturn(Optional.of(t));
        mockMvc.perform(get("/api/v1/reports/templates/" + id))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnActiveTemplates() throws Exception {
        when(service.getActiveTemplates()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reports/templates/active"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldGenerateTemplates() throws Exception {
        when(service.generateTemplates()).thenReturn(List.of());
        mockMvc.perform(post("/api/v1/reports/templates/generate"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnSchedules() throws Exception {
        when(service.listSchedules()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reports/schedules"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldCreateSchedule() throws Exception {
        ReportSchedule s = ReportSchedule.create("Test", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com");
        when(service.createSchedule(anyString(), anyString(), anyString(), any(), anyString(), anyString(), anyString()))
            .thenReturn(s);
        mockMvc.perform(post("/api/v1/reports/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Test","reportId":"r1","reportTitle":"T",
                     "frequency":"DAILY","cronExpression":"","exportFormat":"PDF",
                     "recipientEmail":"a@b.com"}
                    """))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldPauseSchedule() throws Exception {
        ReportSchedule s = ReportSchedule.create("T", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com").withActive(false);
        when(service.pauseSchedule(anyString())).thenReturn(s);
        mockMvc.perform(post("/api/v1/reports/schedules/123/pause"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldResumeSchedule() throws Exception {
        ReportSchedule s = ReportSchedule.create("T", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com").withActive(true);
        when(service.resumeSchedule(anyString())).thenReturn(s);
        mockMvc.perform(post("/api/v1/reports/schedules/123/resume"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldExecuteSchedule() throws Exception {
        ReportSchedule s = ReportSchedule.create("T", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com");
        when(service.executeNow(anyString())).thenReturn(s);
        mockMvc.perform(post("/api/v1/reports/schedules/123/execute"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldDeleteSchedule() throws Exception {
        mockMvc.perform(delete("/api/v1/reports/schedules/123"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldExportReport() throws Exception {
        ReportExport e = ReportExport.create("r1", "T", ExportFormat.PDF, "f.pdf", 100L);
        when(service.exportReport(anyString(), any())).thenReturn(e);
        mockMvc.perform(post("/api/v1/reports/r1/export/PDF"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnBiReports() throws Exception {
        when(service.listBiReports()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reports/bi"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldGenerateBiReports() throws Exception {
        when(service.generateBiReports()).thenReturn(List.of());
        mockMvc.perform(post("/api/v1/reports/bi/generate"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnCacheInfo() throws Exception {
        when(service.getCacheInfo()).thenReturn(Map.of("enabled", true, "activeEntries", 0));
        mockMvc.perform(get("/api/v1/reports/cache"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldClearCache() throws Exception {
        mockMvc.perform(delete("/api/v1/reports/cache"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnTelemetry() throws Exception {
        when(service.getTelemetry()).thenReturn(Map.of("reportRequests", 0L));
        mockMvc.perform(get("/api/v1/reports/telemetry"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnTelemetryHistory() throws Exception {
        when(service.getTelemetryHistory()).thenReturn(Map.of("metrics", Map.of(), "recentActivity", List.of()));
        mockMvc.perform(get("/api/v1/reports/telemetry/history"))
            .andExpect(status().isOk());
    }
}
