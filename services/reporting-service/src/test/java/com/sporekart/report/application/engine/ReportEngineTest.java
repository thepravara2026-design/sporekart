package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReportEngineTest {
    private ReportEngine engine;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        engine = new ReportEngine(repository);
    }

    @Test
    void shouldGenerateAllReports() {
        List<Report> reports = engine.generateAllReports();
        assertFalse(reports.isEmpty());
        assertTrue(reports.size() >= 17); // one per category
    }

    @Test
    void shouldGenerateReportsForCategory() {
        List<Report> reports = engine.generateReportsForCategory(ReportCategory.EXECUTIVE);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().allMatch(r -> r.category() == ReportCategory.EXECUTIVE));
    }

    @Test
    void shouldGenerateRevenueReports() {
        List<Report> reports = engine.generateReportsForCategory(ReportCategory.REVENUE);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().allMatch(r -> r.category() == ReportCategory.REVENUE));
    }

    @Test
    void shouldGenerateSalesReports() {
        List<Report> reports = engine.generateReportsForCategory(ReportCategory.SALES);
        assertFalse(reports.isEmpty());
        assertEquals(ReportCategory.SALES, reports.get(0).category());
    }

    @Test
    void shouldGenerateOrderReports() {
        List<Report> reports = engine.generateReportsForCategory(ReportCategory.ORDERS);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().allMatch(r -> r.category() == ReportCategory.ORDERS));
    }

    @Test
    void shouldGenerateInventoryReports() {
        List<Report> reports = engine.generateReportsForCategory(ReportCategory.INVENTORY);
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldSaveReportsToRepository() {
        engine.generateAllReports();
        assertFalse(repository.findAllReports().isEmpty());
    }

    @Test
    void shouldGenerateCustomReport() {
        Report report = engine.generateReport("Custom Report", "Desc", ReportType.CUSTOM,
            ReportCategory.BUSINESS_HEALTH, "Owner", "Summary", "HEALTHY",
            List.of("Rec"), List.of("Risk"),
            java.util.Map.of("kpi", 1.0), java.util.Map.of("m", 2.0), "template-1");
        assertNotNull(report.id());
        assertEquals(ReportType.CUSTOM, report.type());
        assertEquals(ReportCategory.BUSINESS_HEALTH, report.category());
        assertTrue(report.executionTimeMs() > 0);
    }
}
