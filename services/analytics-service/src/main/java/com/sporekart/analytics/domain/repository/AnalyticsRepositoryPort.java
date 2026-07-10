package com.sporekart.analytics.domain.repository;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;

import java.util.List;

public interface AnalyticsRepositoryPort {
    DashboardWidget saveWidget(DashboardWidget widget);

    List<DashboardWidget> findAllWidgets();

    ReportRequest saveReport(ReportRequest report);

    List<ReportRequest> findAllReports();

    SeoMetadata saveSeoMetadata(SeoMetadata metadata);

    List<SeoMetadata> findAllSeoMetadata();
}
