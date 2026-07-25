package com.sporekart.report.infrastructure.persistence;

import com.sporekart.report.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryReportRepositoryTest {
    private InMemoryReportRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
    }

    @Test
    void shouldSaveAndFindReport() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(report);
        Optional<Report> found = repository.findReportById(report.id());
        assertTrue(found.isPresent());
        assertEquals(report.id(), found.get().id());
    }

    @Test
    void shouldFindAllReports() {
        repository.saveReport(Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1"));
        repository.saveReport(Report.create("R2", "", ReportType.WEEKLY, ReportCategory.REVENUE,
            "Fin", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1"));
        assertEquals(2, repository.findAllReports().size());
    }

    @Test
    void shouldDeleteReport() {
        Report r = Report.create("Del", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r);
        repository.deleteReport(r.id());
        assertTrue(repository.findReportById(r.id()).isEmpty());
    }

    @Test
    void shouldFilterReportsByType() {
        Report r1 = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("R2", "", ReportType.WEEKLY, ReportCategory.REVENUE,
            "Fin", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        repository.saveReport(r2);
        assertEquals(1, repository.findReportsByType(ReportType.EXECUTIVE).size());
        assertEquals(1, repository.findReportsByType(ReportType.WEEKLY).size());
    }

    @Test
    void shouldFilterReportsByCategory() {
        Report r1 = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("R2", "", ReportType.WEEKLY, ReportCategory.REVENUE,
            "Fin", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r1);
        repository.saveReport(r2);
        assertEquals(1, repository.findReportsByCategory(ReportCategory.EXECUTIVE).size());
        assertEquals(1, repository.findReportsByCategory(ReportCategory.REVENUE).size());
    }

    @Test
    void shouldFilterReportsByStatus() {
        Report r = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r);
        assertEquals(1, repository.findReportsByStatus(ReportStatus.GENERATED).size());
    }

    @Test
    void shouldFilterByOwner() {
        Report r = Report.create("R1", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        repository.saveReport(r);
        assertEquals(1, repository.findReportsByOwner("CEO").size());
    }

    @Test
    void shouldSaveAndFindTemplate() {
        ReportTemplate t = ReportTemplate.create("T1", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), Map.of());
        repository.saveTemplate(t);
        assertTrue(repository.findTemplateById(t.id()).isPresent());
    }

    @Test
    void shouldSaveAndFindSchedule() {
        ReportSchedule s = ReportSchedule.create("S1", "r1", "T", ScheduleFrequency.DAILY,
            "", ExportFormat.PDF, "a@b.com");
        repository.saveSchedule(s);
        assertTrue(repository.findScheduleById(s.id()).isPresent());
    }

    @Test
    void shouldSaveAndFindExport() {
        ReportExport e = ReportExport.create("r1", "T", ExportFormat.PDF, "f.pdf", 100L);
        repository.saveExport(e);
        assertTrue(repository.findExportById(e.id()).isPresent());
    }

    @Test
    void shouldSaveAndFindBiReport() {
        BusinessIntelligenceReport r = BusinessIntelligenceReport.create("BI", "",
            ReportType.EXECUTIVE, ReportCategory.EXECUTIVE, "", "", List.of(), List.of(),
            Map.of(), Map.of(), List.of());
        repository.saveBiReport(r);
        assertTrue(repository.findBiReportById(r.id()).isPresent());
    }

    @Test
    void shouldManageCache() {
        ReportCache c = ReportCache.create("type", "key", "data", 300);
        repository.saveCache(c);
        assertTrue(repository.findCacheByKey("key").isPresent());
        repository.deleteCache("key");
        assertTrue(repository.findCacheByKey("key").isEmpty());
        repository.saveCache(ReportCache.create("type", "k1", "d1", 300));
        repository.saveCache(ReportCache.create("type", "k2", "d2", 300));
        repository.clearCache();
        assertEquals(0, repository.findAllCacheEntries().size());
    }
}
