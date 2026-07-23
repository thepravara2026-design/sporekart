package com.sporekart.executive.copilot.service;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.*;
import com.sporekart.executive.copilot.engine.*;
import com.sporekart.executive.copilot.infrastructure.monitoring.ExecutiveMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExecutiveOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(ExecutiveOrchestrator.class);

    private final CompanyHealthEngine companyHealthEngine;
    private final FinancialIntelligenceEngine financialIntelligenceEngine;
    private final BusinessForecastingEngine businessForecastingEngine;
    private final StrategicDecisionEngine strategicDecisionEngine;
    private final RiskIntelligenceEngine riskIntelligenceEngine;
    private final MarketIntelligenceEngine marketIntelligenceEngine;
    private final BoardReportEngine boardReportEngine;
    private final ExecutiveDashboardEngine executiveDashboardEngine;
    private final NaturalLanguageEngine naturalLanguageEngine;
    private final PerformanceAnalyticsEngine performanceAnalyticsEngine;
    private final ExecutiveMetricsService metricsService;

    public ExecutiveOrchestrator(CompanyHealthEngine companyHealthEngine,
                                  FinancialIntelligenceEngine financialIntelligenceEngine,
                                  BusinessForecastingEngine businessForecastingEngine,
                                  StrategicDecisionEngine strategicDecisionEngine,
                                  RiskIntelligenceEngine riskIntelligenceEngine,
                                  MarketIntelligenceEngine marketIntelligenceEngine,
                                  BoardReportEngine boardReportEngine,
                                  ExecutiveDashboardEngine executiveDashboardEngine,
                                  NaturalLanguageEngine naturalLanguageEngine,
                                  PerformanceAnalyticsEngine performanceAnalyticsEngine,
                                  ExecutiveMetricsService metricsService) {
        this.companyHealthEngine = companyHealthEngine;
        this.financialIntelligenceEngine = financialIntelligenceEngine;
        this.businessForecastingEngine = businessForecastingEngine;
        this.strategicDecisionEngine = strategicDecisionEngine;
        this.riskIntelligenceEngine = riskIntelligenceEngine;
        this.marketIntelligenceEngine = marketIntelligenceEngine;
        this.boardReportEngine = boardReportEngine;
        this.executiveDashboardEngine = executiveDashboardEngine;
        this.naturalLanguageEngine = naturalLanguageEngine;
        this.performanceAnalyticsEngine = performanceAnalyticsEngine;
        this.metricsService = metricsService;
    }

    public ExecutiveQueryResponse processQuery(ExecutiveQueryRequest request) {
        long start = System.currentTimeMillis();
        log.info("Processing executive query: intent={}", request.intent());
        try {
            return naturalLanguageEngine.processQuery(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
            metricsService.recordPromptUsage();
        }
    }

    public DashboardResponse getDashboard() {
        long start = System.currentTimeMillis();
        log.info("Generating executive dashboard");
        try {
            return executiveDashboardEngine.getDashboard();
        } finally {
            metricsService.recordDashboardUsage("executive");
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public Map<String, Object> getTodaySummary() {
        metricsService.recordDashboardUsage("today");
        return executiveDashboardEngine.getTodaySummary();
    }

    public Map<String, Object> getWeeklyReport() {
        metricsService.recordDashboardUsage("weekly");
        return executiveDashboardEngine.getWeeklyReport();
    }

    public Map<String, Object> getMonthlyReport() {
        metricsService.recordDashboardUsage("monthly");
        return executiveDashboardEngine.getMonthlyReport();
    }

    public ReportResponse generateReport(ReportRequest request) {
        long start = System.currentTimeMillis();
        log.info("Generating report: type={} period={}", request.reportType(), request.period());
        try {
            return boardReportEngine.generateReport(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
            metricsService.recordPromptUsage();
        }
    }

    public BoardReport generateBoardReport(String period) {
        return boardReportEngine.generateBoardReport(period);
    }

    public ForecastResponse forecast(ForecastRequest request) {
        long start = System.currentTimeMillis();
        log.info("Forecasting: type={} horizon={}", request.forecastType(), request.horizonMonths());
        try {
            metricsService.recordForecastExecution(System.currentTimeMillis() - start);
            return businessForecastingEngine.forecast(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public BusinessForecast generateDetailedForecast(String type, int months) {
        return businessForecastingEngine.generateDetailedForecast(type, months);
    }

    public Map<String, Object> forecastRevenue(int months) {
        return businessForecastingEngine.forecastRevenue(months);
    }

    public Map<String, Object> forecastOrders(int months) {
        return businessForecastingEngine.forecastOrders(months);
    }

    public RiskResponse assessRisks(RiskRequest request) {
        long start = System.currentTimeMillis();
        log.info("Assessing risks: category={}", request.category());
        try {
            return riskIntelligenceEngine.assessRisks(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public RiskMatrix getRiskMatrix() {
        return riskIntelligenceEngine.getRiskMatrix();
    }

    public Map<String, Object> getTopRisks(int count) {
        return riskIntelligenceEngine.getTopRisks(count);
    }

    public Map<String, Object> detectRevenueDeclineRisk() {
        return riskIntelligenceEngine.detectRevenueDeclineRisk();
    }

    public HealthResponse getHealth() {
        long start = System.currentTimeMillis();
        log.info("Getting company health");
        try {
            return companyHealthEngine.getHealthReport();
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public CompanyHealth calculateOverallHealth() {
        return companyHealthEngine.calculateOverallHealth();
    }

    public CompanyHealth getDimensionHealth(String dimension) {
        return companyHealthEngine.getDimensionHealth(dimension);
    }

    public BusinessSustainability assessSustainability() {
        return companyHealthEngine.assessSustainability();
    }

    public CashFlowIndicators analyzeCashFlow() {
        return companyHealthEngine.analyzeCashFlow();
    }

    public FinancialResponse analyzeFinancials(String metric, String period) {
        long start = System.currentTimeMillis();
        log.info("Analyzing financials: metric={}", metric);
        try {
            return financialIntelligenceEngine.analyzeFinancials(metric, period);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public RevenueBreakdown getRevenueBreakdown() {
        return financialIntelligenceEngine.getRevenueBreakdown();
    }

    public ProfitAnalysis getProfitAnalysis() {
        return financialIntelligenceEngine.getProfitAnalysis();
    }

    public double calculateROI(double investment, double return_) {
        return financialIntelligenceEngine.calculateROI(investment, return_);
    }

    public Map<String, Object> getFinancialTrends(String period) {
        return financialIntelligenceEngine.getFinancialTrends(period);
    }

    public DecisionResponse getStrategicDecision(DecisionRequest request) {
        long start = System.currentTimeMillis();
        log.info("Strategic decision: intent={}", request.intent());
        try {
            return strategicDecisionEngine.getRecommendation(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public StrategicRecommendation createRecommendation(String title, String description,
                                                         StrategicRecommendation.RecommendationCategory category,
                                                         double roi, double impact, double confidence) {
        return strategicDecisionEngine.createRecommendation(title, description, category, roi, impact, confidence);
    }

    public List<StrategicRecommendation> prioritizeRecommendations(List<StrategicRecommendation> recs) {
        return strategicDecisionEngine.prioritizeRecommendations(recs);
    }

    public Map<String, Object> simulateDecisionImpact(String decision, double investment) {
        return strategicDecisionEngine.simulateDecisionImpact(decision, investment);
    }

    public PerformanceResponse analyzePerformance(PerformanceRequest request) {
        long start = System.currentTimeMillis();
        log.info("Performance analysis: department={}", request.department());
        try {
            return performanceAnalyticsEngine.analyzePerformance(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public DepartmentPerformance getDepartmentDetail(String department) {
        return performanceAnalyticsEngine.getDepartmentDetail(department);
    }

    public List<PerformanceMetric> getCrossDepartmentalMetrics() {
        return performanceAnalyticsEngine.getCrossDepartmentalMetrics();
    }

    public MarketResponse analyzeMarket(MarketRequest request) {
        long start = System.currentTimeMillis();
        log.info("Market analysis: segment={}", request.segment());
        try {
            return marketIntelligenceEngine.analyzeMarket(request);
        } finally {
            metricsService.recordQueryLatency(System.currentTimeMillis() - start);
        }
    }

    public MarketIntelligence getDetailedIntelligence(String segment) {
        return marketIntelligenceEngine.getDetailedIntelligence(segment);
    }

    public Map<String, Object> benchmarkCompetitors(String metric) {
        return marketIntelligenceEngine.benchmarkCompetitors(metric);
    }

    public List<String> getIndustryTrends(String sector) {
        return marketIntelligenceEngine.getIndustryTrends(sector);
    }
}
