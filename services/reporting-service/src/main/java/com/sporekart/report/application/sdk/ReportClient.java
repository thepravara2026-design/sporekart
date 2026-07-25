package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.application.service.ReportingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportClient implements ReportSDK {
    private final ReportingService service;

    public ReportClient(ReportingService service) {
        this.service = service;
    }

    @Override
    public List<Report> getAllReports() { return service.listReports(); }

    @Override
    public Optional<Report> getReportById(String id) { return service.getReport(id); }

    @Override
    public List<Report> getReportsByType(ReportType type) { return service.getReportsByType(type); }

    @Override
    public List<Report> getReportsByCategory(ReportCategory category) { return service.getReportsByCategory(category); }

    @Override
    public List<Report> getReportsByStatus(ReportStatus status) { return service.getReportsByStatus(status); }

    @Override
    public List<Report> generateAllReports() { return service.generateAllReports(); }

    @Override
    public List<Report> generateReportsForCategory(ReportCategory category) { return service.generateReportsForCategory(category); }

    @Override
    public Report generateReport(String title, String description, ReportType type, ReportCategory category,
                                  String owner, String summary, String businessHealth,
                                  List<String> recommendations, List<String> risks,
                                  Map<String, Object> kpis, Map<String, Object> supportingMetrics,
                                  String templateId) {
        return service.generateReport(title, description, type, category, owner, summary,
            businessHealth, recommendations, risks, kpis, supportingMetrics, templateId);
    }

    @Override
    public List<ReportTemplate> getAllTemplates() { return service.listTemplates(); }

    @Override
    public Optional<ReportTemplate> getTemplateById(String id) { return service.getTemplate(id); }

    @Override
    public List<ReportTemplate> getActiveTemplates() { return service.getActiveTemplates(); }

    @Override
    public ReportTemplate createTemplate(String name, String description, ReportCategory category,
                                          ReportType type, String owner, List<String> sections,
                                          Map<String, Object> defaultConfig) {
        return service.createTemplate(name, description, category, type, owner, sections, defaultConfig);
    }

    @Override
    public List<ReportSchedule> getAllSchedules() { return service.listSchedules(); }

    @Override
    public Optional<ReportSchedule> getScheduleById(String id) { return service.getSchedule(id); }

    @Override
    public List<ReportSchedule> getActiveSchedules() { return service.getActiveSchedules(); }

    @Override
    public ReportSchedule createSchedule(String name, String reportId, String reportTitle,
                                          ScheduleFrequency frequency, String cronExpression,
                                          String exportFormat, String recipientEmail) {
        return service.createSchedule(name, reportId, reportTitle, frequency, cronExpression,
            exportFormat, recipientEmail);
    }

    @Override
    public ReportSchedule pauseSchedule(String scheduleId) { return service.pauseSchedule(scheduleId); }

    @Override
    public ReportSchedule resumeSchedule(String scheduleId) { return service.resumeSchedule(scheduleId); }

    @Override
    public void deleteSchedule(String scheduleId) { service.deleteSchedule(scheduleId); }

    @Override
    public ReportSchedule executeNow(String scheduleId) { return service.executeNow(scheduleId); }

    @Override
    public List<ReportExport> getAllExports() { return service.listExports(); }

    @Override
    public Optional<ReportExport> getExportById(String id) { return service.getExport(id); }

    @Override
    public ReportExport exportReport(String reportId, ExportFormat format) { return service.exportReport(reportId, format); }

    @Override
    public List<BusinessIntelligenceReport> getAllBiReports() { return service.listBiReports(); }

    @Override
    public Optional<BusinessIntelligenceReport> getBiReportById(String id) { return service.getBiReport(id); }

    @Override
    public List<BusinessIntelligenceReport> generateBiReports() { return service.generateBiReports(); }

    @Override
    public Map<String, Object> getCacheInfo() { return service.getCacheInfo(); }

    @Override
    public void clearCache() { service.clearCache(); }

    @Override
    public Map<String, Object> getTelemetry() { return service.getTelemetry(); }

    @Override
    public Map<String, Object> health() { return service.health(); }
}
