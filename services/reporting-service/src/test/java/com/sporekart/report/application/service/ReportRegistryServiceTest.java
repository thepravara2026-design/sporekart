package com.sporekart.report.application.service;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportRegistryServiceTest {
    private ReportRegistryService registry;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        registry = new ReportRegistryService(repository);
    }

    @Test
    void shouldReturnEmptyWhenNoReports() {
        assertTrue(registry.getAllReports().isEmpty());
    }

    @Test
    void shouldReturnReportAfterSave() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(report);
        assertFalse(registry.getAllReports().isEmpty());
        assertTrue(registry.getReportById(report.id()).isPresent());
    }

    @Test
    void shouldFilterByType() {
        Report r1 = Report.create("Exec", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("Weekly", "", ReportType.WEEKLY, ReportCategory.REVENUE,
            "Fin", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        repository.saveReport(r2);
        assertEquals(1, registry.getReportsByType(ReportType.EXECUTIVE).size());
        assertEquals(1, registry.getReportsByType(ReportType.WEEKLY).size());
    }

    @Test
    void shouldFilterByCategory() {
        Report r1 = Report.create("Exec", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("Rev", "", ReportType.WEEKLY, ReportCategory.REVENUE,
            "Fin", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        repository.saveReport(r2);
        assertEquals(1, registry.getReportsByCategory(ReportCategory.EXECUTIVE).size());
    }

    @Test
    void shouldFilterByStatus() {
        Report r1 = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        assertEquals(1, registry.getReportsByStatus(ReportStatus.GENERATED).size());
    }

    @Test
    void shouldFilterByOwner() {
        Report r1 = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        assertEquals(1, registry.getReportsByOwner("CEO").size());
    }

    @Test
    void shouldReturnTemplates() {
        ReportTemplate t = ReportTemplate.create("Template", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), Map.of());
        repository.saveTemplate(t);
        assertFalse(registry.getAllTemplates().isEmpty());
        assertTrue(registry.getTemplateById(t.id()).isPresent());
    }

    @Test
    void shouldReturnActiveTemplates() {
        ReportTemplate t = ReportTemplate.create("T", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), Map.of());
        repository.saveTemplate(t);
        assertFalse(registry.getActiveTemplates().isEmpty());
    }

    @Test
    void shouldReturnSchedules() {
        ReportSchedule s = ReportSchedule.create("S", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com");
        repository.saveSchedule(s);
        assertFalse(registry.getAllSchedules().isEmpty());
    }

    @Test
    void shouldReturnActiveSchedules() {
        ReportSchedule s = ReportSchedule.create("S", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com");
        repository.saveSchedule(s);
        assertFalse(registry.getActiveSchedules().isEmpty());
    }

    @Test
    void shouldReturnBiReports() {
        BusinessIntelligenceReport r = BusinessIntelligenceReport.create("BI", "",
            ReportType.EXECUTIVE, ReportCategory.EXECUTIVE, "", "", List.of(), List.of(),
            Map.of(), Map.of(), List.of());
        repository.saveBiReport(r);
        assertFalse(registry.getAllBiReports().isEmpty());
    }

    @Test
    void shouldReturnExports() {
        ReportExport e = ReportExport.create("r1", "T", ExportFormat.PDF, "f.pdf", 100L);
        repository.saveExport(e);
        assertFalse(registry.getAllExports().isEmpty());
    }
}
