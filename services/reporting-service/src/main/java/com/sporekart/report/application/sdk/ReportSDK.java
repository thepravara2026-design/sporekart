package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReportSDK {
    // Reports
    List<Report> getAllReports();
    Optional<Report> getReportById(String id);
    List<Report> getReportsByType(ReportType type);
    List<Report> getReportsByCategory(ReportCategory category);
    List<Report> getReportsByStatus(ReportStatus status);
    List<Report> generateAllReports();
    List<Report> generateReportsForCategory(ReportCategory category);
    Report generateReport(String title, String description, ReportType type, ReportCategory category,
                          String owner, String summary, String businessHealth,
                          List<String> recommendations, List<String> risks,
                          Map<String, Object> kpis, Map<String, Object> supportingMetrics,
                          String templateId);

    // Templates
    List<ReportTemplate> getAllTemplates();
    Optional<ReportTemplate> getTemplateById(String id);
    List<ReportTemplate> getActiveTemplates();
    ReportTemplate createTemplate(String name, String description, ReportCategory category,
                                   ReportType type, String owner, List<String> sections,
                                   Map<String, Object> defaultConfig);

    // Schedules
    List<ReportSchedule> getAllSchedules();
    Optional<ReportSchedule> getScheduleById(String id);
    List<ReportSchedule> getActiveSchedules();
    ReportSchedule createSchedule(String name, String reportId, String reportTitle,
                                   ScheduleFrequency frequency, String cronExpression,
                                   String exportFormat, String recipientEmail);
    ReportSchedule pauseSchedule(String scheduleId);
    ReportSchedule resumeSchedule(String scheduleId);
    void deleteSchedule(String scheduleId);
    ReportSchedule executeNow(String scheduleId);

    // Exports
    List<ReportExport> getAllExports();
    Optional<ReportExport> getExportById(String id);
    ReportExport exportReport(String reportId, ExportFormat format);

    // BI Reports
    List<BusinessIntelligenceReport> getAllBiReports();
    Optional<BusinessIntelligenceReport> getBiReportById(String id);
    List<BusinessIntelligenceReport> generateBiReports();

    // Cache
    Map<String, Object> getCacheInfo();
    void clearCache();

    // Telemetry
    Map<String, Object> getTelemetry();

    // Health
    Map<String, Object> health();
}
