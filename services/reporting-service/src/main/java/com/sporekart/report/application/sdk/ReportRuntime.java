package com.sporekart.report.application.sdk;

import com.sporekart.report.application.engine.ReportEngine;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportRuntime {
    private final ReportEngine reportEngine;
    private final ReportRepositoryPort repository;

    public ReportRuntime(ReportEngine reportEngine, ReportRepositoryPort repository) {
        this.reportEngine = reportEngine;
        this.repository = repository;
    }

    public List<Report> generateAllReports() {
        List<Report> reports = reportEngine.generateAllReports();
        reports.forEach(repository::saveReport);
        return reports;
    }

    public List<Report> generateReportsForCategory(ReportCategory category) {
        List<Report> reports = reportEngine.generateReportsForCategory(category);
        reports.forEach(repository::saveReport);
        return reports;
    }

    public Report generateReport(String title, String description, ReportType type, ReportCategory category,
                                  String owner, String summary, String businessHealth,
                                  List<String> recommendations, List<String> risks,
                                  Map<String, Object> kpis, Map<String, Object> supportingMetrics,
                                  String templateId) {
        return reportEngine.generateReport(title, description, type, category, owner,
            summary, businessHealth, recommendations, risks, kpis, supportingMetrics, templateId);
    }

    public List<Report> getAllReports() { return repository.findAllReports(); }
    public Optional<Report> findById(String id) { return repository.findReportById(id); }
    public List<Report> findByType(ReportType type) { return repository.findReportsByType(type); }
    public List<Report> findByCategory(ReportCategory category) { return repository.findReportsByCategory(category); }
    public List<Report> findByStatus(ReportStatus status) { return repository.findReportsByStatus(status); }
}
