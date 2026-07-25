package com.sporekart.report.application.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReportTelemetryService {
    private final AtomicLong reportRequests = new AtomicLong(0);
    private final AtomicLong exportRequests = new AtomicLong(0);
    private final AtomicLong scheduleRequests = new AtomicLong(0);
    private final AtomicLong templateUsageCount = new AtomicLong(0);
    private final AtomicLong totalRuntimeMs = new AtomicLong(0);
    private final AtomicLong errors = new AtomicLong(0);
    private final AtomicLong downloadRequests = new AtomicLong(0);
    private final Map<String, AtomicLong> reportByType = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> reportByCategory = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> history = Collections.synchronizedList(new ArrayList<>());

    public void recordReportRequest(String type, String category, long runtimeMs) {
        reportRequests.incrementAndGet();
        totalRuntimeMs.addAndGet(runtimeMs);
        reportByType.computeIfAbsent(type, k -> new AtomicLong(0)).incrementAndGet();
        reportByCategory.computeIfAbsent(category, k -> new AtomicLong(0)).incrementAndGet();
        if (history.size() > 1000) history.remove(0);
        history.add(Map.of(
            "type", "report_request",
            "reportType", type,
            "category", category,
            "runtimeMs", runtimeMs,
            "timestamp", System.currentTimeMillis()
        ));
    }

    public void recordExportRequest() { exportRequests.incrementAndGet(); }
    public void recordScheduleRequest() { scheduleRequests.incrementAndGet(); }
    public void recordTemplateUsage() { templateUsageCount.incrementAndGet(); }
    public void recordError() { errors.incrementAndGet(); }
    public void recordDownloadRequest() { downloadRequests.incrementAndGet(); }

    public void recordReportRequest(String type, long runtimeMs) {
        recordReportRequest(type, "UNKNOWN", runtimeMs);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("reportRequests", reportRequests.get());
        metrics.put("exportRequests", exportRequests.get());
        metrics.put("scheduleRequests", scheduleRequests.get());
        metrics.put("templateUsage", templateUsageCount.get());
        metrics.put("totalRuntimeMs", totalRuntimeMs.get());
        metrics.put("errors", errors.get());
        metrics.put("downloadRequests", downloadRequests.get());
        metrics.put("reportByType", new LinkedHashMap<>(reportByType));
        metrics.put("reportByCategory", new LinkedHashMap<>(reportByCategory));
        return metrics;
    }

    public Map<String, Object> getHistory() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("metrics", getMetrics());
        result.put("recentActivity", List.copyOf(history));
        return result;
    }
}
