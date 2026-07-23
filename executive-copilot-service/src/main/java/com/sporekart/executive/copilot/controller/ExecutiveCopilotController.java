package com.sporekart.executive.copilot.controller;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.*;
import com.sporekart.executive.copilot.service.ExecutiveOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/copilot/executive")
public class ExecutiveCopilotController {

    private static final Logger log = LoggerFactory.getLogger(ExecutiveCopilotController.class);

    private final ExecutiveOrchestrator orchestrator;

    public ExecutiveCopilotController(ExecutiveOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/chat")
    public ResponseEntity<ExecutiveCopilotResponse<ExecutiveQueryResponse>> chat(
            @Valid @RequestBody ExecutiveQueryRequest request) {
        log.info("POST /chat intent={}", request.intent());
        var response = orchestrator.processQuery(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping(value = "/stream")
    public ResponseEntity<ExecutiveCopilotResponse<ExecutiveQueryResponse>> stream(
            @Valid @RequestBody ExecutiveQueryRequest request) {
        log.info("POST /stream intent={}", request.intent());
        var response = orchestrator.processQuery(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ExecutiveCopilotResponse<DashboardResponse>> dashboard() {
        log.info("GET /dashboard");
        var response = orchestrator.getDashboard();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/dashboard/today")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> todaySummary() {
        var response = orchestrator.getTodaySummary();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/dashboard/weekly")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> weeklyReport() {
        var response = orchestrator.getWeeklyReport();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/dashboard/monthly")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> monthlyReport() {
        var response = orchestrator.getMonthlyReport();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/report")
    public ResponseEntity<ExecutiveCopilotResponse<ReportResponse>> report(
            @Valid @RequestBody ReportRequest request) {
        log.info("POST /report type={}", request.reportType());
        var response = orchestrator.generateReport(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/forecast")
    public ResponseEntity<ExecutiveCopilotResponse<ForecastResponse>> forecast(
            @Valid @RequestBody ForecastRequest request) {
        log.info("POST /forecast type={}", request.forecastType());
        var response = orchestrator.forecast(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/risk")
    public ResponseEntity<ExecutiveCopilotResponse<RiskResponse>> risk(
            @Valid @RequestBody RiskRequest request) {
        log.info("POST /risk category={}", request.category());
        var response = orchestrator.assessRisks(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/health")
    public ResponseEntity<ExecutiveCopilotResponse<HealthResponse>> health() {
        log.info("GET /health");
        var response = orchestrator.getHealth();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/financial")
    public ResponseEntity<ExecutiveCopilotResponse<FinancialResponse>> financial(
            @Valid @RequestBody FinancialRequest request) {
        log.info("POST /financial metric={}", request.metric());
        var response = orchestrator.analyzeFinancials(request.metric(), request.period());
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/decision")
    public ResponseEntity<ExecutiveCopilotResponse<DecisionResponse>> decision(
            @Valid @RequestBody DecisionRequest request) {
        log.info("POST /decision intent={}", request.intent());
        var response = orchestrator.getStrategicDecision(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/performance")
    public ResponseEntity<ExecutiveCopilotResponse<PerformanceResponse>> performance(
            @Valid @RequestBody PerformanceRequest request) {
        log.info("POST /performance department={}", request.department());
        var response = orchestrator.analyzePerformance(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @PostMapping("/market")
    public ResponseEntity<ExecutiveCopilotResponse<MarketResponse>> market(
            @Valid @RequestBody MarketRequest request) {
        log.info("POST /market segment={}", request.segment());
        var response = orchestrator.analyzeMarket(request);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/risk/matrix")
    public ResponseEntity<ExecutiveCopilotResponse<RiskMatrix>> riskMatrix() {
        var response = orchestrator.getRiskMatrix();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/risk/top")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> topRisks(
            @RequestParam(defaultValue = "5") int count) {
        var response = orchestrator.getTopRisks(count);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/risk/revenue-decline")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> revenueDeclineRisk() {
        var response = orchestrator.detectRevenueDeclineRisk();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/health/dimension/{dimension}")
    public ResponseEntity<ExecutiveCopilotResponse<CompanyHealth>> dimensionHealth(
            @PathVariable String dimension) {
        var response = orchestrator.getDimensionHealth(dimension);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/health/sustainability")
    public ResponseEntity<ExecutiveCopilotResponse<BusinessSustainability>> sustainability() {
        var response = orchestrator.assessSustainability();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/health/cashflow")
    public ResponseEntity<ExecutiveCopilotResponse<CashFlowIndicators>> cashFlow() {
        var response = orchestrator.analyzeCashFlow();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/financial/revenue-breakdown")
    public ResponseEntity<ExecutiveCopilotResponse<RevenueBreakdown>> revenueBreakdown() {
        var response = orchestrator.getRevenueBreakdown();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/financial/profit-analysis")
    public ResponseEntity<ExecutiveCopilotResponse<ProfitAnalysis>> profitAnalysis() {
        var response = orchestrator.getProfitAnalysis();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/financial/trends")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> financialTrends(
            @RequestParam(defaultValue = "quarterly") String period) {
        var response = orchestrator.getFinancialTrends(period);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/forecast/revenue")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> forecastRevenue(
            @RequestParam(defaultValue = "12") int months) {
        var response = orchestrator.forecastRevenue(months);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/forecast/orders")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> forecastOrders(
            @RequestParam(defaultValue = "12") int months) {
        var response = orchestrator.forecastOrders(months);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/decision/simulate")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> simulateDecision(
            @RequestParam String decision,
            @RequestParam double investment) {
        var response = orchestrator.simulateDecisionImpact(decision, investment);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/market/benchmark")
    public ResponseEntity<ExecutiveCopilotResponse<Map<String, Object>>> benchmark(
            @RequestParam(defaultValue = "overall") String metric) {
        var response = orchestrator.benchmarkCompetitors(metric);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/market/trends")
    public ResponseEntity<ExecutiveCopilotResponse<List<String>>> industryTrends(
            @RequestParam(defaultValue = "mushroom_cultivation") String sector) {
        var response = orchestrator.getIndustryTrends(sector);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/performance/department/{department}")
    public ResponseEntity<ExecutiveCopilotResponse<DepartmentPerformance>> departmentDetail(
            @PathVariable String department) {
        var response = orchestrator.getDepartmentDetail(department);
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }

    @GetMapping("/performance/cross-departmental")
    public ResponseEntity<ExecutiveCopilotResponse<List<PerformanceMetric>>> crossDepartmentalMetrics() {
        var response = orchestrator.getCrossDepartmentalMetrics();
        return ResponseEntity.ok(ExecutiveCopilotResponse.success(response));
    }
}
