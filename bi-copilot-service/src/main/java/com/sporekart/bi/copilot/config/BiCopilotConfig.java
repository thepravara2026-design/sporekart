package com.sporekart.bi.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.bi.copilot")
public class BiCopilotConfig {

    private boolean enabled = true;
    private int dataRetentionDays = 90;
    private List<String> reportFormats = List.of("PDF", "EXCEL", "CSV", "HTML");
    private Duration dashboardRefreshInterval = Duration.ofSeconds(300);
    private int forecastHorizonDays = 365;
    private int maxQueryResultSize = 10000;
    private Duration cacheTtl = Duration.ofMinutes(5);
    private boolean auditEnabled = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getDataRetentionDays() { return dataRetentionDays; }
    public void setDataRetentionDays(int dataRetentionDays) { this.dataRetentionDays = dataRetentionDays; }
    public List<String> getReportFormats() { return reportFormats; }
    public void setReportFormats(List<String> reportFormats) { this.reportFormats = reportFormats; }
    public Duration getDashboardRefreshInterval() { return dashboardRefreshInterval; }
    public void setDashboardRefreshInterval(Duration dashboardRefreshInterval) { this.dashboardRefreshInterval = dashboardRefreshInterval; }
    public int getForecastHorizonDays() { return forecastHorizonDays; }
    public void setForecastHorizonDays(int forecastHorizonDays) { this.forecastHorizonDays = forecastHorizonDays; }
    public int getMaxQueryResultSize() { return maxQueryResultSize; }
    public void setMaxQueryResultSize(int maxQueryResultSize) { this.maxQueryResultSize = maxQueryResultSize; }
    public Duration getCacheTtl() { return cacheTtl; }
    public void setCacheTtl(Duration cacheTtl) { this.cacheTtl = cacheTtl; }
    public boolean isAuditEnabled() { return auditEnabled; }
    public void setAuditEnabled(boolean auditEnabled) { this.auditEnabled = auditEnabled; }
}
