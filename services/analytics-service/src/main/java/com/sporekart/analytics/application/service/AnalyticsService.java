package com.sporekart.analytics.application.service;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import com.sporekart.analytics.domain.repository.AnalyticsRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final AnalyticsRepositoryPort repositoryPort;

    public AnalyticsService(AnalyticsRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = repositoryPort.getDashboard();
        if (dashboard == null) {
            dashboard = new HashMap<>();
        }
        dashboard.put("widgets", repositoryPort.findAllWidgets());
        return dashboard;
    }

    public Map<String, Object> getSalesMetrics() {
        Map<String, Object> metrics = repositoryPort.getSalesMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getCustomerMetrics() {
        Map<String, Object> metrics = repositoryPort.getCustomerMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getInventoryMetrics() {
        Map<String, Object> metrics = repositoryPort.getInventoryMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getPaymentMetrics() {
        Map<String, Object> metrics = repositoryPort.getPaymentMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getShipmentMetrics() {
        Map<String, Object> metrics = repositoryPort.getShipmentMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getTrainingMetrics() {
        Map<String, Object> metrics = repositoryPort.getTrainingMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public Map<String, Object> getGrowerMetrics() {
        Map<String, Object> metrics = repositoryPort.getGrowerMetrics();
        return metrics != null ? metrics : new HashMap<>();
    }

    public List<DashboardWidget> getWidgets() {
        return repositoryPort.findAllWidgets();
    }

    public DashboardWidget createWidget(String name, String metric) {
        return repositoryPort.saveWidget(DashboardWidget.create(name, metric));
    }

    public List<ReportRequest> getReports() {
        return repositoryPort.findAllReports();
    }

    public ReportRequest createReport(String reportType, String format) {
        return repositoryPort.saveReport(ReportRequest.create(reportType, format));
    }

    public List<SeoMetadata> getSeoMetadata() {
        return repositoryPort.findAllSeoMetadata();
    }

    public SeoMetadata createSeoMetadata(String path, String title, String description) {
        return repositoryPort.saveSeoMetadata(SeoMetadata.create(path, title, description));
    }
}
