package com.sporekart.report.interfaces.rest;

import com.sporekart.report.application.service.ReportingService;
import com.sporekart.report.domain.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportingService service;

    public ReportController(ReportingService service) {
        this.service = service;
    }

    // --- Health ---
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(service.health());
    }

    // --- Reports ---
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(service.listReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable String id) {
        return service.getReport(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Report>> getReportsByType(@PathVariable ReportType type) {
        return ResponseEntity.ok(service.getReportsByType(type));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Report>> getReportsByCategory(@PathVariable ReportCategory category) {
        return ResponseEntity.ok(service.getReportsByCategory(category));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable ReportStatus status) {
        return ResponseEntity.ok(service.getReportsByStatus(status));
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Report>> generateAllReports() {
        return ResponseEntity.ok(service.generateAllReports());
    }

    @PostMapping("/generate/{category}")
    public ResponseEntity<List<Report>> generateReportsForCategory(@PathVariable ReportCategory category) {
        return ResponseEntity.ok(service.generateReportsForCategory(category));
    }

    // --- Templates ---
    @GetMapping("/templates")
    public ResponseEntity<List<ReportTemplate>> getAllTemplates() {
        return ResponseEntity.ok(service.listTemplates());
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<ReportTemplate> getTemplateById(@PathVariable String id) {
        return service.getTemplate(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/templates/active")
    public ResponseEntity<List<ReportTemplate>> getActiveTemplates() {
        return ResponseEntity.ok(service.getActiveTemplates());
    }

    @PostMapping("/templates/generate")
    public ResponseEntity<List<ReportTemplate>> generateTemplates() {
        return ResponseEntity.ok(service.generateTemplates());
    }

    // --- Schedules ---
    @GetMapping("/schedules")
    public ResponseEntity<List<ReportSchedule>> getAllSchedules() {
        return ResponseEntity.ok(service.listSchedules());
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<ReportSchedule> getScheduleById(@PathVariable String id) {
        return service.getSchedule(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/schedules/active")
    public ResponseEntity<List<ReportSchedule>> getActiveSchedules() {
        return ResponseEntity.ok(service.getActiveSchedules());
    }

    @PostMapping("/schedules")
    public ResponseEntity<ReportSchedule> createSchedule(@RequestBody Map<String, String> body) {
        ReportSchedule schedule = service.createSchedule(
            body.get("name"), body.get("reportId"), body.get("reportTitle"),
            ScheduleFrequency.valueOf(body.getOrDefault("frequency", "DAILY").toUpperCase()),
            body.get("cronExpression"),
            body.getOrDefault("exportFormat", "PDF"),
            body.get("recipientEmail")
        );
        return ResponseEntity.ok(schedule);
    }

    @PostMapping("/schedules/{id}/pause")
    public ResponseEntity<ReportSchedule> pauseSchedule(@PathVariable String id) {
        return ResponseEntity.ok(service.pauseSchedule(id));
    }

    @PostMapping("/schedules/{id}/resume")
    public ResponseEntity<ReportSchedule> resumeSchedule(@PathVariable String id) {
        return ResponseEntity.ok(service.resumeSchedule(id));
    }

    @PostMapping("/schedules/{id}/execute")
    public ResponseEntity<ReportSchedule> executeSchedule(@PathVariable String id) {
        return ResponseEntity.ok(service.executeNow(id));
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        service.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    // --- Exports ---
    @GetMapping("/exports")
    public ResponseEntity<List<ReportExport>> getAllExports() {
        return ResponseEntity.ok(service.listExports());
    }

    @GetMapping("/exports/{id}")
    public ResponseEntity<ReportExport> getExportById(@PathVariable String id) {
        return service.getExport(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{reportId}/export/{format}")
    public ResponseEntity<ReportExport> exportReport(@PathVariable String reportId,
                                                      @PathVariable ExportFormat format) {
        return ResponseEntity.ok(service.exportReport(reportId, format));
    }

    // --- BI Reports ---
    @GetMapping("/bi")
    public ResponseEntity<List<BusinessIntelligenceReport>> getAllBiReports() {
        return ResponseEntity.ok(service.listBiReports());
    }

    @GetMapping("/bi/{id}")
    public ResponseEntity<BusinessIntelligenceReport> getBiReportById(@PathVariable String id) {
        return service.getBiReport(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/bi/generate")
    public ResponseEntity<List<BusinessIntelligenceReport>> generateBiReports() {
        return ResponseEntity.ok(service.generateBiReports());
    }

    // --- Cache ---
    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        return ResponseEntity.ok(service.getCacheInfo());
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        service.clearCache();
        return ResponseEntity.noContent().build();
    }

    // --- Telemetry ---
    @GetMapping("/telemetry")
    public ResponseEntity<Map<String, Object>> getTelemetry() {
        return ResponseEntity.ok(service.getTelemetry());
    }

    @GetMapping("/telemetry/history")
    public ResponseEntity<Map<String, Object>> getTelemetryHistory() {
        return ResponseEntity.ok(service.getTelemetryHistory());
    }
}
