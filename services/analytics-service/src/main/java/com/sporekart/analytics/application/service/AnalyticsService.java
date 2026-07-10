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
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("revenueToday", 125000.0);
        dashboard.put("ordersToday", 42);
        dashboard.put("conversionRate", 3.8);
        dashboard.put("widgets", repositoryPort.findAllWidgets());
        return dashboard;
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
