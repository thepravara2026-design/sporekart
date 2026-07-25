package com.sporekart.report.application.service;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportRegistryService {
    private final ReportRepositoryPort repository;

    public ReportRegistryService(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    // Reports
    public List<Report> getAllReports() { return repository.findAllReports(); }
    public Optional<Report> getReportById(String id) { return repository.findReportById(id); }
    public List<Report> getReportsByType(ReportType type) { return repository.findReportsByType(type); }
    public List<Report> getReportsByCategory(ReportCategory category) { return repository.findReportsByCategory(category); }
    public List<Report> getReportsByStatus(ReportStatus status) { return repository.findReportsByStatus(status); }
    public List<Report> getReportsByOwner(String owner) { return repository.findReportsByOwner(owner); }

    // Templates
    public List<ReportTemplate> getAllTemplates() { return repository.findAllTemplates(); }
    public Optional<ReportTemplate> getTemplateById(String id) { return repository.findTemplateById(id); }
    public List<ReportTemplate> getTemplatesByCategory(ReportCategory category) { return repository.findTemplatesByCategory(category); }
    public List<ReportTemplate> getTemplatesByType(ReportType type) { return repository.findTemplatesByType(type); }
    public List<ReportTemplate> getActiveTemplates() { return repository.findActiveTemplates(); }

    // Schedules
    public List<ReportSchedule> getAllSchedules() { return repository.findAllSchedules(); }
    public Optional<ReportSchedule> getScheduleById(String id) { return repository.findScheduleById(id); }
    public List<ReportSchedule> getSchedulesByFrequency(ScheduleFrequency frequency) { return repository.findSchedulesByFrequency(frequency); }
    public List<ReportSchedule> getActiveSchedules() { return repository.findActiveSchedules(); }

    // Exports
    public List<ReportExport> getAllExports() { return repository.findAllExports(); }
    public Optional<ReportExport> getExportById(String id) { return repository.findExportById(id); }
    public List<ReportExport> getExportsByReportId(String reportId) { return repository.findExportsByReportId(reportId); }
    public List<ReportExport> getExportsByFormat(ExportFormat format) { return repository.findExportsByFormat(format); }

    // BI Reports
    public List<BusinessIntelligenceReport> getAllBiReports() { return repository.findAllBiReports(); }
    public Optional<BusinessIntelligenceReport> getBiReportById(String id) { return repository.findBiReportById(id); }
    public List<BusinessIntelligenceReport> getBiReportsByCategory(ReportCategory category) { return repository.findBiReportsByCategory(category); }
}
