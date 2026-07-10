package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;
import com.sporekart.analytics.domain.repository.AnalyticsRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAnalyticsRepository implements AnalyticsRepositoryPort {
    private final Map<String, DashboardWidget> widgets = new ConcurrentHashMap<>();
    private final Map<String, ReportRequest> reports = new ConcurrentHashMap<>();
    private final Map<String, SeoMetadata> seoMetadata = new ConcurrentHashMap<>();

    @Override
    public DashboardWidget saveWidget(DashboardWidget widget) {
        widgets.put(widget.getId(), widget);
        return widget;
    }

    @Override
    public List<DashboardWidget> findAllWidgets() {
        return new ArrayList<>(widgets.values());
    }

    @Override
    public ReportRequest saveReport(ReportRequest report) {
        reports.put(report.getId(), report);
        return report;
    }

    @Override
    public List<ReportRequest> findAllReports() {
        return new ArrayList<>(reports.values());
    }

    @Override
    public SeoMetadata saveSeoMetadata(SeoMetadata metadata) {
        seoMetadata.put(metadata.getId(), metadata);
        return metadata;
    }

    @Override
    public List<SeoMetadata> findAllSeoMetadata() {
        return new ArrayList<>(seoMetadata.values());
    }
}
