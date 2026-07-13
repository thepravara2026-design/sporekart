package com.sporekart.ai.analytics.interfaces.rest;

import com.sporekart.ai.analytics.api.*;
import com.sporekart.ai.analytics.domain.*;
import com.sporekart.ai.analytics.interfaces.rest.dto.*;
import com.sporekart.ai.analytics.infrastructure.kafka.AnalyticsKafkaEventPublisher;
import com.sporekart.ai.analytics.infrastructure.monitoring.AnalyticsMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/governance")
@RequiredArgsConstructor
public class AnalyticsController {

    private final GovernanceAnalyticsService governanceAnalyticsService;
    private final GovernanceReportingService governanceReportingService;
    private final DashboardService dashboardService;
    private final MetricsAggregationService metricsAggregationService;
    private final KPIService kpiService;
    private final TrendAnalysisService trendAnalysisService;
    private final ExportService exportService;
    private final AnalyticsKafkaEventPublisher eventPublisher;
    private final AnalyticsMonitoringService monitoringService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboard() {
        GovernanceDashboard dashboard = dashboardService.getDashboard();
        List<WidgetDto> widgets = dashboard.widgets().stream()
            .map(w -> new WidgetDto(
                w.id().toString(), w.title(), w.type(), w.metricName(),
                w.configuration(), w.position()
            )).toList();
        return ResponseEntity.ok(new DashboardDto(
            dashboard.id().toString(), dashboard.name(), dashboard.description(),
            widgets, dashboard.configuration(), dashboard.createdAt().toString()
        ));
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<MetricDto>> getMetrics(
        @RequestParam(required = false) String module) {
        List<GovernanceMetric> metrics = module != null
            ? metricsAggregationService.getMetricsByModule(module)
            : metricsAggregationService.getAllMetrics();
        return ResponseEntity.ok(metrics.stream()
            .map(m -> new MetricDto(
                m.id().toString(), m.name(), m.module(), m.type().name(),
                m.value(), m.labels(), m.recordedAt().toString()
            )).toList());
    }

    @GetMapping("/kpis")
    public ResponseEntity<List<KpiDto>> getKPIs() {
        List<GovernanceKPI> kpis = kpiService.getAllKPIs();
        return ResponseEntity.ok(kpis.stream()
            .map(k -> new KpiDto(
                k.id().toString(), k.name(), k.description(), k.module(),
                k.currentValue(), k.targetValue(), k.status().name(),
                k.calculatedAt().toString()
            )).toList());
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TrendDto>> getTrends() {
        List<GovernanceTrend> trends = trendAnalysisService.getAllTrends();
        return ResponseEntity.ok(trends.stream()
            .map(t -> new TrendDto(
                t.id().toString(), t.name(), t.module(), t.dataPoints(),
                t.timestamps().stream().map(Object::toString).toList(),
                t.direction().name(), t.changePercentage()
            )).toList());
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getReports() {
        List<GovernanceReport> reports = governanceReportingService.getAllReports();
        return ResponseEntity.ok(reports.stream()
            .map(r -> new ReportDto(
                r.id().toString(), r.type().name(), r.title(), r.description(),
                r.summary(), r.generatedAt().toString()
            )).toList());
    }

    @PostMapping("/reports")
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportRequestDto request) {
        monitoringService.recordRequest();
        GovernanceReport report = governanceReportingService.generateReport(
            ReportType.valueOf(request.type()), request.title(),
            request.description(), request.params()
        );
        eventPublisher.publishReportGenerated(report.id().toString(), report.type().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReportDto(
            report.id().toString(), report.type().name(), report.title(),
            report.description(), report.summary(), report.generatedAt().toString()
        ));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportDto> getReport(@PathVariable UUID id) {
        GovernanceReport report = governanceReportingService.getReport(id);
        return ResponseEntity.ok(new ReportDto(
            report.id().toString(), report.type().name(), report.title(),
            report.description(), report.summary(), report.generatedAt().toString()
        ));
    }

    @PostMapping("/reports/export")
    public ResponseEntity<ExportDto> exportReport(@RequestBody ExportRequestDto request) {
        monitoringService.recordRequest();
        GovernanceExport export = exportService.exportReport(
            request.reportId(), ReportFormat.valueOf(request.format())
        );
        eventPublisher.publishReportExported(
            export.id().toString(), export.reportId().toString(), export.format().name()
        );
        return ResponseEntity.ok(new ExportDto(
            export.id().toString(), export.reportId(), export.format().name(),
            export.fileName(), export.fileSize(), export.exportedAt().toString()
        ));
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatsDto> getStatistics() {
        List<GovernanceMetric> metrics = metricsAggregationService.getAllMetrics();
        List<GovernanceReport> reports = governanceReportingService.getAllReports();
        List<GovernanceKPI> kpis = kpiService.getAllKPIs();
        List<GovernanceExport> exports = exportService.getAllExports();
        return ResponseEntity.ok(new StatsDto(
            metrics.size(), reports.size(), kpis.size(), exports.size(), Map.of()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDto> health() {
        monitoringService.recordRequest();
        return ResponseEntity.ok(new HealthDto(
            "UP", "analytics", System.currentTimeMillis(), Map.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }
}
