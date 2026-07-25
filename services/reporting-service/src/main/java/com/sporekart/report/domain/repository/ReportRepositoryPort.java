package com.sporekart.report.domain.repository;

import com.sporekart.report.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface ReportRepositoryPort {
    // Reports
    List<Report> findAllReports();
    Optional<Report> findReportById(String id);
    List<Report> findReportsByType(ReportType type);
    List<Report> findReportsByCategory(ReportCategory category);
    List<Report> findReportsByStatus(ReportStatus status);
    List<Report> findReportsByOwner(String owner);
    Report saveReport(Report report);
    void deleteReport(String id);

    // Templates
    List<ReportTemplate> findAllTemplates();
    Optional<ReportTemplate> findTemplateById(String id);
    List<ReportTemplate> findTemplatesByCategory(ReportCategory category);
    List<ReportTemplate> findTemplatesByType(ReportType type);
    List<ReportTemplate> findActiveTemplates();
    ReportTemplate saveTemplate(ReportTemplate template);

    // Schedules
    List<ReportSchedule> findAllSchedules();
    Optional<ReportSchedule> findScheduleById(String id);
    List<ReportSchedule> findSchedulesByFrequency(ScheduleFrequency frequency);
    List<ReportSchedule> findActiveSchedules();
    ReportSchedule saveSchedule(ReportSchedule schedule);
    void deleteSchedule(String id);

    // Exports
    List<ReportExport> findAllExports();
    Optional<ReportExport> findExportById(String id);
    List<ReportExport> findExportsByReportId(String reportId);
    List<ReportExport> findExportsByFormat(ExportFormat format);
    ReportExport saveExport(ReportExport export);

    // BI Reports
    List<BusinessIntelligenceReport> findAllBiReports();
    Optional<BusinessIntelligenceReport> findBiReportById(String id);
    List<BusinessIntelligenceReport> findBiReportsByCategory(ReportCategory category);
    BusinessIntelligenceReport saveBiReport(BusinessIntelligenceReport report);

    // Cache
    List<ReportCache> findAllCacheEntries();
    Optional<ReportCache> findCacheByKey(String key);
    ReportCache saveCache(ReportCache cache);
    void deleteCache(String key);
    void clearCache();
}
