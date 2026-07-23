package com.sporekart.bi.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.bi")
public class BiCopilotConfig {

    private int dataRetentionDays = 365;
    private int forecastDefaultHorizon = 90;
    private int dashboardRefreshSeconds = 300;
    private String executiveReportFormat = "pdf,html,json";
    private Map<String, Double> healthScoreWeights;

    public int getDataRetentionDays() {
        return dataRetentionDays;
    }

    public void setDataRetentionDays(int dataRetentionDays) {
        this.dataRetentionDays = dataRetentionDays;
    }

    public int getForecastDefaultHorizon() {
        return forecastDefaultHorizon;
    }

    public void setForecastDefaultHorizon(int forecastDefaultHorizon) {
        this.forecastDefaultHorizon = forecastDefaultHorizon;
    }

    public int getDashboardRefreshSeconds() {
        return dashboardRefreshSeconds;
    }

    public void setDashboardRefreshSeconds(int dashboardRefreshSeconds) {
        this.dashboardRefreshSeconds = dashboardRefreshSeconds;
    }

    public String getExecutiveReportFormat() {
        return executiveReportFormat;
    }

    public void setExecutiveReportFormat(String executiveReportFormat) {
        this.executiveReportFormat = executiveReportFormat;
    }

    public Map<String, Double> getHealthScoreWeights() {
        return healthScoreWeights;
    }

    public void setHealthScoreWeights(Map<String, Double> healthScoreWeights) {
        this.healthScoreWeights = healthScoreWeights;
    }
}
