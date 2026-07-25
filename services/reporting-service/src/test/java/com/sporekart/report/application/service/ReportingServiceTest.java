package com.sporekart.report.application.service;

import com.sporekart.report.config.ReportConfig;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.application.engine.*;
import com.sporekart.report.infrastructure.cache.ReportCacheService;
import com.sporekart.report.infrastructure.export.MockExportService;
import com.sporekart.report.infrastructure.scheduler.MockSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportingServiceTest {
    private ReportingService service;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new com.sporekart.report.infrastructure.persistence.InMemoryReportRepository();
        ReportEngine reportEngine = new ReportEngine(repository);
        TemplateEngine templateEngine = new TemplateEngine(repository);
        BusinessIntelligenceRuntime biRuntime = new BusinessIntelligenceRuntime(repository);
        ReportRegistryService registryService = new ReportRegistryService(repository);
        ReportCacheService cacheService = new ReportCacheService(repository);
        ReportTelemetryService telemetry = new ReportTelemetryService();
        MockExportService exportService = new MockExportService(repository);
        MockSchedulerService schedulerService = new MockSchedulerService(repository);
        ReportConfig config = new ReportConfig();
        service = new ReportingService(repository, reportEngine, templateEngine, biRuntime,
            registryService, cacheService, telemetry, exportService, schedulerService, config);
    }

    @Test
    void shouldReturnHealth() {
        Map<String, Object> health = service.health();
        assertEquals("UP", health.get("status"));
        assertEquals("reporting-service", health.get("service"));
    }

    @Test
    void shouldGenerateAllReports() {
        var reports = service.generateAllReports();
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldListReportsAfterGeneration() {
        service.generateAllReports();
        assertFalse(service.listReports().isEmpty());
    }

    @Test
    void shouldGenerateTemplates() {
        var templates = service.generateTemplates();
        assertFalse(templates.isEmpty());
    }

    @Test
    void shouldGenerateBiReports() {
        var biReports = service.generateBiReports();
        assertFalse(biReports.isEmpty());
    }

    @Test
    void shouldCreateSchedule() {
        service.generateAllReports();
        String reportId = service.listReports().get(0).id();
        ReportSchedule s = service.createSchedule("Test", reportId, "Title",
            ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        assertNotNull(s.id());
    }

    @Test
    void shouldExportReport() {
        service.generateAllReports();
        String reportId = service.listReports().get(0).id();
        ReportExport export = service.exportReport(reportId, ExportFormat.PDF);
        assertNotNull(export.id());
    }

    @Test
    void shouldGetTelemetry() {
        Map<String, Object> telemetry = service.getTelemetry();
        assertNotNull(telemetry.get("reportRequests"));
    }

    @Test
    void shouldGetCacheInfo() {
        Map<String, Object> cache = service.getCacheInfo();
        assertNotNull(cache.get("enabled"));
    }

    @Test
    void shouldClearCache() {
        service.clearCache();
        Map<String, Object> cache = service.getCacheInfo();
        assertEquals(0, cache.get("totalEntries"));
    }

    @Test
    void shouldGetReportById() {
        service.generateAllReports();
        String id = service.listReports().get(0).id();
        assertTrue(service.getReport(id).isPresent());
    }

    @Test
    void shouldReturnEmptyForMissingReport() {
        assertTrue(service.getReport("non-existent").isEmpty());
    }

    @Test
    void shouldGetReportsByStatus() {
        service.generateAllReports();
        assertFalse(service.getReportsByStatus(ReportStatus.GENERATED).isEmpty());
    }
}
