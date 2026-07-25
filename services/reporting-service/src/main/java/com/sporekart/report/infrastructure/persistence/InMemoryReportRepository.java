package com.sporekart.report.infrastructure.persistence;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryReportRepository implements ReportRepositoryPort {
    private final Map<String, Report> reports = new ConcurrentHashMap<>();
    private final Map<String, ReportTemplate> templates = new ConcurrentHashMap<>();
    private final Map<String, ReportSchedule> schedules = new ConcurrentHashMap<>();
    private final Map<String, ReportExport> exports = new ConcurrentHashMap<>();
    private final Map<String, BusinessIntelligenceReport> biReports = new ConcurrentHashMap<>();
    private final Map<String, ReportCache> cache = new ConcurrentHashMap<>();

    @Override
    public List<Report> findAllReports() {
        return List.copyOf(reports.values());
    }

    @Override
    public Optional<Report> findReportById(String id) {
        return Optional.ofNullable(reports.get(id));
    }

    @Override
    public List<Report> findReportsByType(ReportType type) {
        return reports.values().stream()
            .filter(r -> r.type() == type)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Report> findReportsByCategory(ReportCategory category) {
        return reports.values().stream()
            .filter(r -> r.category() == category)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Report> findReportsByStatus(ReportStatus status) {
        return reports.values().stream()
            .filter(r -> r.status() == status)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Report> findReportsByOwner(String owner) {
        return reports.values().stream()
            .filter(r -> r.owner().equalsIgnoreCase(owner))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized Report saveReport(Report report) {
        reports.put(report.id(), report);
        return report;
    }

    @Override
    public synchronized void deleteReport(String id) {
        reports.remove(id);
    }

    @Override
    public List<ReportTemplate> findAllTemplates() {
        return List.copyOf(templates.values());
    }

    @Override
    public Optional<ReportTemplate> findTemplateById(String id) {
        return Optional.ofNullable(templates.get(id));
    }

    @Override
    public List<ReportTemplate> findTemplatesByCategory(ReportCategory category) {
        return templates.values().stream()
            .filter(t -> t.category() == category)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ReportTemplate> findTemplatesByType(ReportType type) {
        return templates.values().stream()
            .filter(t -> t.type() == type)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ReportTemplate> findActiveTemplates() {
        return templates.values().stream()
            .filter(ReportTemplate::active)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized ReportTemplate saveTemplate(ReportTemplate template) {
        templates.put(template.id(), template);
        return template;
    }

    @Override
    public List<ReportSchedule> findAllSchedules() {
        return List.copyOf(schedules.values());
    }

    @Override
    public Optional<ReportSchedule> findScheduleById(String id) {
        return Optional.ofNullable(schedules.get(id));
    }

    @Override
    public List<ReportSchedule> findSchedulesByFrequency(ScheduleFrequency frequency) {
        return schedules.values().stream()
            .filter(s -> s.frequency() == frequency)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ReportSchedule> findActiveSchedules() {
        return schedules.values().stream()
            .filter(ReportSchedule::active)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized ReportSchedule saveSchedule(ReportSchedule schedule) {
        schedules.put(schedule.id(), schedule);
        return schedule;
    }

    @Override
    public synchronized void deleteSchedule(String id) {
        schedules.remove(id);
    }

    @Override
    public List<ReportExport> findAllExports() {
        return List.copyOf(exports.values());
    }

    @Override
    public Optional<ReportExport> findExportById(String id) {
        return Optional.ofNullable(exports.get(id));
    }

    @Override
    public List<ReportExport> findExportsByReportId(String reportId) {
        return exports.values().stream()
            .filter(e -> e.reportId().equals(reportId))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ReportExport> findExportsByFormat(ExportFormat format) {
        return exports.values().stream()
            .filter(e -> e.format() == format)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized ReportExport saveExport(ReportExport export) {
        exports.put(export.id(), export);
        return export;
    }

    @Override
    public List<BusinessIntelligenceReport> findAllBiReports() {
        return List.copyOf(biReports.values());
    }

    @Override
    public Optional<BusinessIntelligenceReport> findBiReportById(String id) {
        return Optional.ofNullable(biReports.get(id));
    }

    @Override
    public List<BusinessIntelligenceReport> findBiReportsByCategory(ReportCategory category) {
        return biReports.values().stream()
            .filter(r -> r.category() == category)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized BusinessIntelligenceReport saveBiReport(BusinessIntelligenceReport report) {
        biReports.put(report.id(), report);
        return report;
    }

    @Override
    public List<ReportCache> findAllCacheEntries() {
        return List.copyOf(cache.values());
    }

    @Override
    public Optional<ReportCache> findCacheByKey(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public synchronized ReportCache saveCache(ReportCache cacheEntry) {
        cache.put(cacheEntry.cacheKey(), cacheEntry);
        return cacheEntry;
    }

    @Override
    public synchronized void deleteCache(String key) {
        cache.remove(key);
    }

    @Override
    public synchronized void clearCache() {
        cache.clear();
    }
}
