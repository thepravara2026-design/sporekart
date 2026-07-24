package com.sporekart.analytics.domain.repository;

import com.sporekart.analytics.domain.model.DashboardWidget;
import com.sporekart.analytics.domain.model.ReportRequest;
import com.sporekart.analytics.domain.model.SeoMetadata;

import java.util.List;
import java.util.Map;

public interface AnalyticsRepositoryPort {
    DashboardWidget saveWidget(DashboardWidget widget);

    List<DashboardWidget> findAllWidgets();

    ReportRequest saveReport(ReportRequest report);

    List<ReportRequest> findAllReports();

    SeoMetadata saveSeoMetadata(SeoMetadata metadata);

    List<SeoMetadata> findAllSeoMetadata();

    Map<String, Object> getDashboard();

    Map<String, Object> getSalesMetrics();

    Map<String, Object> getCustomerMetrics();

    Map<String, Object> getInventoryMetrics();

    Map<String, Object> getPaymentMetrics();

    Map<String, Object> getShipmentMetrics();

    Map<String, Object> getTrainingMetrics();

    Map<String, Object> getGrowerMetrics();
}
