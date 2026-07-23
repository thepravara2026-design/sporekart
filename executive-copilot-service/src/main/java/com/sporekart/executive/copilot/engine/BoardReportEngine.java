package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.ReportRequest;
import com.sporekart.executive.copilot.dto.ReportResponse;
import com.sporekart.executive.copilot.dto.ReportResponse.RiskItemBrief;
import com.sporekart.executive.copilot.dto.ReportResponse.OpportunityItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BoardReportEngine {

    private static final Logger log = LoggerFactory.getLogger(BoardReportEngine.class);

    public ReportResponse generateReport(ReportRequest request) {
        log.info("Generating {} report for period: {}", request.reportType(), request.period());
        var period = request.period() != null ? request.period() : "current_quarter";
        var reportId = UUID.randomUUID().toString();
        var title = "SporeKart " + request.reportType().substring(0, 1).toUpperCase() + request.reportType().substring(1) + " Report - " + period;
        var execSummary = generateExecutiveSummary(request.reportType(), period);
        var financialSummary = new LinkedHashMap<String, Object>();
        financialSummary.put("totalRevenue", 12500000.0);
        financialSummary.put("netProfit", 3000000.0);
        financialSummary.put("grossMargin", 40.0);
        financialSummary.put("netMargin", 24.0);
        financialSummary.put("revenueGrowth", 13.6);
        financialSummary.put("profitGrowth", 20.0);
        financialSummary.put("cashPosition", 4500000.0);
        var kpis = new LinkedHashMap<String, Object>();
        kpis.put("orderFulfillmentRate", 94.5);
        kpis.put("customerRetention", 78.0);
        kpis.put("inventoryTurnover", 6.5);
        kpis.put("employeeCount", 85);
        kpis.put("activeCustomers", 12500);
        kpis.put("trainingCompletionRate", 92.0);
        var risks = List.of(
            new RiskItemBrief("Revenue Concentration", "REVENUE", 55.0, "HIGH"),
            new RiskItemBrief("Supply Chain Risk", "SUPPLY_CHAIN", 45.0, "MEDIUM"),
            new RiskItemBrief("Market Competition", "COMPETITIVE", 50.0, "MEDIUM")
        );
        var opportunities = List.of(
            new OpportunityItem("South India Expansion", "Expand into growing South Indian market", 185.0, 0.85),
            new OpportunityItem("B2B Training Vertical", "Launch corporate training programs", 150.0, 0.80)
        );
        var deptStatus = new LinkedHashMap<String, String>();
        deptStatus.put("operations", "On track - efficiency improving");
        deptStatus.put("marketing", "Above target - strong campaign performance");
        deptStatus.put("training", "Exceeding goals - high demand");
        deptStatus.put("inventory", "Needs attention - dead stock increasing");
        deptStatus.put("finance", "Stable - cash position healthy");
        var outlook = "Based on current trajectory, SporeKart is positioned for 15-18% growth in the next fiscal year. "
            + "Key focus areas: market expansion, operational efficiency, and customer retention.";
        return new ReportResponse(reportId, title, period, execSummary, financialSummary, kpis, risks, opportunities, deptStatus, outlook);
    }

    public BoardReport generateBoardReport(String period) {
        log.debug("Generating board report for: {}", period);
        var financialSummary = new LinkedHashMap<String, Object>();
        financialSummary.put("revenue", 12500000.0);
        financialSummary.put("profit", 3000000.0);
        financialSummary.put("margin", 24.0);
        var kpis = new LinkedHashMap<String, Object>();
        kpis.put("customerSatisfaction", 4.5);
        kpis.put("orderAccuracy", 99.2);
        var risks = List.of(
            new BusinessRisk("R1", "Revenue concentration", "Top products dominate", BusinessRisk.RiskCategory.REVENUE,
                65.0, 45.0, 55.0, "62% from top 3 products", List.of("Diversify"), "ACTIVE")
        );
        var opportunities = List.of(
            new StrategicRecommendation("O1", "Market Expansion", "Enter South India",
                StrategicRecommendation.RecommendationCategory.EXPANSION, 185.0, 90.0, 0.85, "6 months",
                List.of("Market analysis"), "LOW", List.of("Research", "Plan"))
        );
        var deptStatus = new LinkedHashMap<String, String>();
        deptStatus.put("Operations", "On track");
        return new BoardReport(UUID.randomUUID().toString(), "Board Report", period, LocalDate.now(),
            generateExecutiveSummary("board", period), financialSummary, kpis, risks, opportunities, deptStatus, "Positive outlook");
    }

    private String generateExecutiveSummary(String type, String period) {
        return "SporeKart delivered strong performance in " + period + " with revenue of Rs. 1.25Cr, "
            + "representing 13.6% YoY growth. Profit margins improved to 24% driven by operational efficiencies "
            + "and favorable product mix. Customer base grew 18% with training segment showing exceptional momentum. "
            + "Key strategic priorities include South India market expansion and warehouse automation investment.";
    }
}
