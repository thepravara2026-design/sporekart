package com.sporekart.report.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportTelemetryServiceTest {
    private ReportTelemetryService telemetry;

    @BeforeEach
    void setUp() {
        telemetry = new ReportTelemetryService();
    }

    @Test
    void shouldRecordReportRequest() {
        telemetry.recordReportRequest("WEEKLY", "REVENUE", 100L);
        Map<String, Object> metrics = telemetry.getMetrics();
        assertEquals(1L, metrics.get("reportRequests"));
        assertEquals(100L, metrics.get("totalRuntimeMs"));
    }

    @Test
    void shouldRecordExportRequest() {
        telemetry.recordExportRequest();
        assertEquals(1L, telemetry.getMetrics().get("exportRequests"));
    }

    @Test
    void shouldRecordScheduleRequest() {
        telemetry.recordScheduleRequest();
        assertEquals(1L, telemetry.getMetrics().get("scheduleRequests"));
    }

    @Test
    void shouldRecordTemplateUsage() {
        telemetry.recordTemplateUsage();
        assertEquals(1L, telemetry.getMetrics().get("templateUsage"));
    }

    @Test
    void shouldRecordError() {
        telemetry.recordError();
        assertEquals(1L, telemetry.getMetrics().get("errors"));
    }

    @Test
    void shouldRecordDownloadRequest() {
        telemetry.recordDownloadRequest();
        assertEquals(1L, telemetry.getMetrics().get("downloadRequests"));
    }

    @Test
    void shouldTrackReportByType() {
        telemetry.recordReportRequest("WEEKLY", "REVENUE", 50L);
        telemetry.recordReportRequest("MONTHLY", "EXECUTIVE", 100L);
        Map<String, Object> metrics = telemetry.getMetrics();
        Map<String, Object> byType = (Map<String, Object>) metrics.get("reportByType");
        assertEquals(1L, ((java.util.concurrent.atomic.AtomicLong) byType.get("WEEKLY")).longValue());
        assertEquals(1L, ((java.util.concurrent.atomic.AtomicLong) byType.get("MONTHLY")).longValue());
    }

    @Test
    void shouldGetHistory() {
        telemetry.recordReportRequest("DAILY", "SALES", 200L);
        Map<String, Object> history = telemetry.getHistory();
        assertNotNull(history.get("metrics"));
        assertNotNull(history.get("recentActivity"));
    }

    @Test
    void shouldStartAtZero() {
        Map<String, Object> metrics = telemetry.getMetrics();
        assertEquals(0L, metrics.get("reportRequests"));
        assertEquals(0L, metrics.get("errors"));
    }

    @Test
    void shouldAccumulateMultipleRequests() {
        telemetry.recordReportRequest("A", "B", 10L);
        telemetry.recordReportRequest("A", "B", 20L);
        telemetry.recordReportRequest("A", "B", 30L);
        assertEquals(3L, telemetry.getMetrics().get("reportRequests"));
        assertEquals(60L, telemetry.getMetrics().get("totalRuntimeMs"));
    }
}
