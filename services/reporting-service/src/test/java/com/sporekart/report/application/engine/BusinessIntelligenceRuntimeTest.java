package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BusinessIntelligenceRuntimeTest {
    private BusinessIntelligenceRuntime biRuntime;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        biRuntime = new BusinessIntelligenceRuntime(repository);
    }

    @Test
    void shouldGenerateAllBiReports() {
        List<BusinessIntelligenceReport> reports = biRuntime.generateBiReports();
        assertFalse(reports.isEmpty());
        assertTrue(reports.size() >= 8);
    }

    @Test
    void shouldGenerateBiForExecutiveCategory() {
        List<BusinessIntelligenceReport> reports = biRuntime.generateBiReportsForCategory(ReportCategory.EXECUTIVE);
        assertFalse(reports.isEmpty());
        assertEquals(ReportCategory.EXECUTIVE, reports.get(0).category());
    }

    @Test
    void shouldGenerateBiForRevenue() {
        List<BusinessIntelligenceReport> reports = biRuntime.generateBiReportsForCategory(ReportCategory.REVENUE);
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldGetAggregatedReport() {
        biRuntime.generateBiReports();
        List<BusinessIntelligenceReport> all = repository.findAllBiReports();
        BusinessIntelligenceReport found = biRuntime.getAggregatedReport(all.get(0).id());
        assertNotNull(found);
        assertEquals(all.get(0).id(), found.id());
    }

    @Test
    void shouldGetAllBiReports() {
        biRuntime.generateBiReports();
        assertFalse(biRuntime.getAllBiReports().isEmpty());
    }

    @Test
    void shouldSaveBiReports() {
        biRuntime.generateBiReports();
        assertFalse(repository.findAllBiReports().isEmpty());
    }

    @Test
    void shouldGenerateBiForCompliance() {
        List<BusinessIntelligenceReport> reports = biRuntime.generateBiReportsForCategory(ReportCategory.COMPLIANCE);
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldGenerateBiForRisk() {
        List<BusinessIntelligenceReport> reports = biRuntime.generateBiReportsForCategory(ReportCategory.RISK);
        assertFalse(reports.isEmpty());
        assertTrue(reports.stream().allMatch(r -> r.category() == ReportCategory.RISK));
    }
}
