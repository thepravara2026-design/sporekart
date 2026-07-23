package com.sporekart.executive.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.executive.copilot")
public class ExecutiveCopilotConfig {

    private String companyName = "SporeKart";
    private String defaultCurrency = "INR";
    private String fiscalYearStart = "2026-04-01";
    private List<String> healthDimensions = List.of("revenue", "profitability", "growth", "customer", "operations", "marketing", "inventory", "training");
    private double riskThresholdCritical = 75;
    private double riskThresholdHigh = 50;
    private double riskThresholdMedium = 25;
    private int forecastHorizonMonths = 12;
    private String boardReportDefaultPeriod = "quarterly";

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency = defaultCurrency; }
    public String getFiscalYearStart() { return fiscalYearStart; }
    public void setFiscalYearStart(String fiscalYearStart) { this.fiscalYearStart = fiscalYearStart; }
    public List<String> getHealthDimensions() { return healthDimensions; }
    public void setHealthDimensions(List<String> healthDimensions) { this.healthDimensions = healthDimensions; }
    public double getRiskThresholdCritical() { return riskThresholdCritical; }
    public void setRiskThresholdCritical(double riskThresholdCritical) { this.riskThresholdCritical = riskThresholdCritical; }
    public double getRiskThresholdHigh() { return riskThresholdHigh; }
    public void setRiskThresholdHigh(double riskThresholdHigh) { this.riskThresholdHigh = riskThresholdHigh; }
    public double getRiskThresholdMedium() { return riskThresholdMedium; }
    public void setRiskThresholdMedium(double riskThresholdMedium) { this.riskThresholdMedium = riskThresholdMedium; }
    public int getForecastHorizonMonths() { return forecastHorizonMonths; }
    public void setForecastHorizonMonths(int forecastHorizonMonths) { this.forecastHorizonMonths = forecastHorizonMonths; }
    public String getBoardReportDefaultPeriod() { return boardReportDefaultPeriod; }
    public void setBoardReportDefaultPeriod(String boardReportDefaultPeriod) { this.boardReportDefaultPeriod = boardReportDefaultPeriod; }
}
