package com.sporekart.report.application.sdk;

import com.sporekart.report.application.engine.ReportEngine;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReportRuntimeTest {
    private ReportRuntime runtime;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        ReportEngine engine = new ReportEngine(repository);
        runtime = new ReportRuntime(engine, repository);
    }

    @Test
    void shouldGenerateAllReports() {
        List<Report> reports = runtime.generateAllReports();
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldGenerateReportsByCategory() {
        List<Report> reports = runtime.generateReportsForCategory(ReportCategory.EXECUTIVE);
        assertFalse(reports.isEmpty());
    }

    @Test
    void shouldGetAllReports() {
        runtime.generateAllReports();
        assertFalse(runtime.getAllReports().isEmpty());
    }

    @Test
    void shouldFindById() {
        runtime.generateAllReports();
        Report first = runtime.getAllReports().get(0);
        assertTrue(runtime.findById(first.id()).isPresent());
    }

    @Test
    void shouldFindByType() {
        runtime.generateAllReports();
        List<Report> execReports = runtime.findByType(ReportType.DAILY);
        assertFalse(execReports.isEmpty());
    }

    @Test
    void shouldFindByCategory() {
        runtime.generateAllReports();
        List<Report> reports = runtime.findByCategory(ReportCategory.REVENUE);
        assertTrue(reports.stream().allMatch(r -> r.category() == ReportCategory.REVENUE));
    }

    @Test
    void shouldFindByStatus() {
        runtime.generateAllReports();
        List<Report> generated = runtime.findByStatus(ReportStatus.GENERATED);
        assertFalse(generated.isEmpty());
        assertTrue(generated.stream().allMatch(r -> r.status() == ReportStatus.GENERATED));
    }

    @Test
    void shouldReturnEmptyForMissingId() {
        assertTrue(runtime.findById("non-existent").isEmpty());
    }
}
