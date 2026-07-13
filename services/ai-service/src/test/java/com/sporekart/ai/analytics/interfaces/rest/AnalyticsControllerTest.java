package com.sporekart.ai.analytics.interfaces.rest;

import com.sporekart.ai.analytics.api.*;
import com.sporekart.ai.analytics.domain.*;
import com.sporekart.ai.analytics.infrastructure.kafka.AnalyticsKafkaEventPublisher;
import com.sporekart.ai.analytics.infrastructure.monitoring.AnalyticsMonitoringService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private GovernanceAnalyticsService governanceAnalyticsService;
    @MockitoBean private GovernanceReportingService governanceReportingService;
    @MockitoBean private DashboardService dashboardService;
    @MockitoBean private MetricsAggregationService metricsAggregationService;
    @MockitoBean private KPIService kpiService;
    @MockitoBean private TrendAnalysisService trendAnalysisService;
    @MockitoBean private ExportService exportService;
    @MockitoBean private AnalyticsKafkaEventPublisher eventPublisher;
    @MockitoBean private AnalyticsMonitoringService monitoringService;

    @Test
    void testGetDashboard() throws Exception {
        var dashboard = new GovernanceDashboard(UUID.randomUUID(), "main", "desc", List.of(), Map.of(), Instant.now(), Instant.now());
        when(dashboardService.getDashboard()).thenReturn(dashboard);

        mockMvc.perform(get("/api/v1/governance/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMetrics() throws Exception {
        var metric = new GovernanceMetric(UUID.randomUUID(), "m1", "mod1", MetricType.COUNT, 42.0, Map.of(), Instant.now());
        when(metricsAggregationService.getAllMetrics()).thenReturn(List.of(metric));

        mockMvc.perform(get("/api/v1/governance/metrics"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetKPIs() throws Exception {
        var kpi = new GovernanceKPI(UUID.randomUUID(), "kpi1", "desc", "mod1", 90.0, 100.0, 0.5, KpiStatus.ON_TRACK, Map.of(), Instant.now());
        when(kpiService.getAllKPIs()).thenReturn(List.of(kpi));

        mockMvc.perform(get("/api/v1/governance/kpis"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrends() throws Exception {
        var trend = new GovernanceTrend(UUID.randomUUID(), "t1", "mod1", List.of(1.0, 2.0), List.of(Instant.now()), TrendDirection.UP, 10.0, Instant.now());
        when(trendAnalysisService.getAllTrends()).thenReturn(Map.of("mod1", List.of(trend)));

        mockMvc.perform(get("/api/v1/governance/trends"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReports() throws Exception {
        var report = new GovernanceReport(UUID.randomUUID(), ReportType.COMPLIANCE_REPORT, "title", "desc", Map.of(), Map.of(), Instant.now(), UUID.randomUUID());
        when(governanceReportingService.getAllReports()).thenReturn(List.of(report));

        mockMvc.perform(get("/api/v1/governance/reports"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateReport() throws Exception {
        var report = new GovernanceReport(UUID.randomUUID(), ReportType.COMPLIANCE_REPORT, "title", "desc", Map.of(), Map.of(), Instant.now(), UUID.randomUUID());
        when(governanceReportingService.generateReport(any(ReportType.class), anyString(), anyString(), any())).thenReturn(report);

        mockMvc.perform(post("/api/v1/governance/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"COMPLIANCE_REPORT","title":"Test","description":"Desc","params":{}}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetReportById() throws Exception {
        var report = new GovernanceReport(UUID.randomUUID(), ReportType.COMPLIANCE_REPORT, "title", "desc", Map.of(), Map.of(), Instant.now(), UUID.randomUUID());
        when(governanceReportingService.getReport(any(UUID.class))).thenReturn(report);

        mockMvc.perform(get("/api/v1/governance/reports/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void testExportReport() throws Exception {
        var export = new GovernanceExport(UUID.randomUUID(), UUID.randomUUID(), ReportFormat.JSON, "report.json", 0L, Map.of(), Instant.now(), UUID.randomUUID());
        when(exportService.exportReport(any(UUID.class), any(ReportFormat.class))).thenReturn(export);

        mockMvc.perform(post("/api/v1/governance/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"reportId":"%s","format":"JSON"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStatistics() throws Exception {
        when(metricsAggregationService.getAllMetrics()).thenReturn(List.of());
        when(governanceReportingService.getAllReports()).thenReturn(List.of());
        when(kpiService.getAllKPIs()).thenReturn(List.of());
        when(exportService.getAllExports()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/governance/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/v1/governance/health"))
                .andExpect(status().isOk());
    }
}
