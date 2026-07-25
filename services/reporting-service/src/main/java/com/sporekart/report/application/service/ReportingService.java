package com.sporekart.report.application.service;

import com.sporekart.report.config.ReportConfig;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.application.engine.*;
import com.sporekart.report.infrastructure.cache.ReportCacheService;
import com.sporekart.report.infrastructure.export.MockExportService;
import com.sporekart.report.infrastructure.scheduler.MockSchedulerService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportingService {
    private final ReportRepositoryPort repository;
    private final ReportEngine reportEngine;
    private final TemplateEngine templateEngine;
    private final BusinessIntelligenceRuntime biRuntime;
    private final ReportRegistryService registryService;
    private final ReportCacheService cacheService;
    private final ReportTelemetryService telemetry;
    private final MockExportService exportService;
    private final MockSchedulerService schedulerService;
    private final ReportConfig config;

    public ReportingService(ReportRepositoryPort repository, ReportEngine reportEngine,
                            TemplateEngine templateEngine, BusinessIntelligenceRuntime biRuntime,
                            ReportRegistryService registryService, ReportCacheService cacheService,
                            ReportTelemetryService telemetry, MockExportService exportService,
                            MockSchedulerService schedulerService, ReportConfig config) {
        this.repository = repository;
        this.reportEngine = reportEngine;
        this.templateEngine = templateEngine;
        this.biRuntime = biRuntime;
        this.registryService = registryService;
        this.cacheService = cacheService;
        this.telemetry = telemetry;
        this.exportService = exportService;
        this.schedulerService = schedulerService;
        this.config = config;
    }

    // --- Report queries ---
    public List<Report> listReports() { return registryService.getAllReports(); }
    public Optional<Report> getReport(String id) { return registryService.getReportById(id); }
    public List<Report> getReportsByType(ReportType type) { return registryService.getReportsByType(type); }
    public List<Report> getReportsByCategory(ReportCategory category) { return registryService.getReportsByCategory(category); }
    public List<Report> getReportsByStatus(ReportStatus status) { return registryService.getReportsByStatus(status); }

    // --- Report generation ---
    public List<Report> generateAllReports() {
        long start = System.currentTimeMillis();
        List<Report> reports = reportEngine.generateAllReports();
        reports.forEach(r -> telemetry.recordReportRequest(r.type().name(), r.category().name(), System.currentTimeMillis() - start));
        return reports;
    }

    public List<Report> generateReportsForCategory(ReportCategory category) {
        long start = System.currentTimeMillis();
        List<Report> reports = reportEngine.generateReportsForCategory(category);
        reports.forEach(r -> telemetry.recordReportRequest(r.type().name(), r.category().name(), System.currentTimeMillis() - start));
        return reports;
    }

    public Report generateReport(String title, String description, ReportType type, ReportCategory category,
                                  String owner, String summary, String businessHealth,
                                  List<String> recommendations, List<String> risks,
                                  Map<String, Object> kpis, Map<String, Object> supportingMetrics,
                                  String templateId) {
        long start = System.currentTimeMillis();
        Report report = reportEngine.generateReport(title, description, type, category, owner,
            summary, businessHealth, recommendations, risks, kpis, supportingMetrics, templateId);
        telemetry.recordReportRequest(type.name(), System.currentTimeMillis() - start);
        return report;
    }

    // --- Templates ---
    public List<ReportTemplate> listTemplates() { return registryService.getAllTemplates(); }
    public Optional<ReportTemplate> getTemplate(String id) { return registryService.getTemplateById(id); }
    public List<ReportTemplate> getActiveTemplates() { return registryService.getActiveTemplates(); }
    public ReportTemplate createTemplate(String name, String description, ReportCategory category,
                                          ReportType type, String owner, List<String> sections,
                                          Map<String, Object> defaultConfig) {
        return templateEngine.createTemplate(name, description, category, type, owner, sections, defaultConfig);
    }
    public List<ReportTemplate> generateTemplates() {
        return templateEngine.generateAllTemplates();
    }

    // --- Schedules ---
    public List<ReportSchedule> listSchedules() { return registryService.getAllSchedules(); }
    public Optional<ReportSchedule> getSchedule(String id) { return registryService.getScheduleById(id); }
    public List<ReportSchedule> getActiveSchedules() { return registryService.getActiveSchedules(); }
    public ReportSchedule createSchedule(String name, String reportId, String reportTitle,
                                          ScheduleFrequency frequency, String cronExpression,
                                          String exportFormat, String recipientEmail) {
        telemetry.recordScheduleRequest();
        return schedulerService.createSchedule(name, reportId, reportTitle, frequency,
            cronExpression, exportFormat, recipientEmail);
    }
    public ReportSchedule pauseSchedule(String id) { return schedulerService.pauseSchedule(id); }
    public ReportSchedule resumeSchedule(String id) { return schedulerService.resumeSchedule(id); }
    public void deleteSchedule(String id) { schedulerService.deleteSchedule(id); }
    public ReportSchedule executeNow(String id) { return schedulerService.executeNow(id); }

    // --- Exports ---
    public List<ReportExport> listExports() { return registryService.getAllExports(); }
    public Optional<ReportExport> getExport(String id) { return registryService.getExportById(id); }
    public ReportExport exportReport(String reportId, ExportFormat format) {
        telemetry.recordExportRequest();
        return exportService.exportReport(reportId, format);
    }

    // --- BI Reports ---
    public List<BusinessIntelligenceReport> listBiReports() { return registryService.getAllBiReports(); }
    public Optional<BusinessIntelligenceReport> getBiReport(String id) { return registryService.getBiReportById(id); }
    public List<BusinessIntelligenceReport> generateBiReports() { return biRuntime.generateBiReports(); }

    // --- Cache ---
    public Map<String, Object> getCacheInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("enabled", cacheService.isEnabled());
        info.put("activeEntries", cacheService.getActiveEntryCount());
        info.put("totalEntries", cacheService.getTotalEntryCount());
        info.put("entries", cacheService.getAllEntries().stream()
            .map(e -> Map.of("cacheKey", e.cacheKey(), "cacheType", e.cacheType(),
                             "expired", e.isExpired(), "hitCount", e.hitCount()))
            .toList());
        return info;
    }
    public void clearCache() { cacheService.clear(); }

    // --- Telemetry ---
    public Map<String, Object> getTelemetry() { return telemetry.getMetrics(); }
    public Map<String, Object> getTelemetryHistory() { return telemetry.getHistory(); }

    // --- Health ---
    public Map<String, Object> health() {
        Map<String, Object> h = new LinkedHashMap<>();
        h.put("status", "UP");
        h.put("service", "reporting-service");
        h.put("version", "0.1.0-SNAPSHOT");
        h.put("reportEngine", config.getEngine().isEnabled() ? "enabled" : "disabled");
        h.put("templateEngine", config.getTemplate().isEnabled() ? "enabled" : "disabled");
        h.put("exportEngine", config.getExport().isEnabled() ? "enabled" : "disabled");
        h.put("scheduler", config.getScheduler().isEnabled() ? "enabled" : "disabled");
        h.put("cache", config.getCache().isEnabled() ? "enabled" : "disabled");
        h.put("bi", config.getBi().isEnabled() ? "enabled" : "disabled");
        h.put("totalReports", repository.findAllReports().size());
        h.put("totalTemplates", repository.findAllTemplates().size());
        h.put("totalSchedules", repository.findAllSchedules().size());
        h.put("totalExports", repository.findAllExports().size());
        h.put("totalBiReports", repository.findAllBiReports().size());
        return h;
    }
}
