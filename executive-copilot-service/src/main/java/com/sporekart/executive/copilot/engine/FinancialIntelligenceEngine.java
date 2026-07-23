package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.FinancialResponse;
import com.sporekart.executive.copilot.dto.FinancialResponse.FinancialMetricItem;
import com.sporekart.executive.copilot.dto.FinancialResponse.RevenueDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FinancialIntelligenceEngine {

    private static final Logger log = LoggerFactory.getLogger(FinancialIntelligenceEngine.class);

    public FinancialResponse analyzeFinancials(String metric, String period) {
        log.info("Analyzing financials for metric: {} period: {}", metric, period);
        var summary = new LinkedHashMap<String, Object>();
        summary.put("totalRevenue", 12500000.0);
        summary.put("totalExpenses", 9500000.0);
        summary.put("netProfit", 3000000.0);
        summary.put("profitMargin", 24.0);
        summary.put("period", period != null ? period : "last_quarter");
        summary.put("currency", "INR");

        var metrics = List.of(
            new FinancialMetricItem("Revenue", 12500000.0, 11000000.0, 15000000.0, "INR", "UP"),
            new FinancialMetricItem("Gross Profit", 5000000.0, 4400000.0, 6000000.0, "INR", "UP"),
            new FinancialMetricItem("Net Profit", 3000000.0, 2500000.0, 4000000.0, "INR", "UP"),
            new FinancialMetricItem("Operating Cost", 6500000.0, 6000000.0, 5500000.0, "INR", "UP"),
            new FinancialMetricItem("ROAS", 5.2, 4.8, 6.0, "x", "UP"),
            new FinancialMetricItem("Average Order Value", 850.0, 780.0, 1000.0, "INR", "UP"),
            new FinancialMetricItem("Customer LTV", 4500.0, 4200.0, 5000.0, "INR", "UP")
        );

        var byCategory = new LinkedHashMap<String, Double>();
        byCategory.put("Spawn Kits", 4500000.0);
        byCategory.put("Grow Kits", 3200000.0);
        byCategory.put("Substrates", 1800000.0);
        byCategory.put("Training", 1500000.0);
        byCategory.put("Accessories", 1500000.0);

        var revenue = new RevenueDetail(12500000.0, 13.6, byCategory, 850.0, 4500.0);
        return new FinancialResponse(summary, metrics, revenue);
    }

    public RevenueBreakdown getRevenueBreakdown() {
        log.info("Getting revenue breakdown");
        var byCategory = new LinkedHashMap<String, Double>();
        byCategory.put("Spawn Kits", 4500000.0);
        byCategory.put("Grow Kits", 3200000.0);
        byCategory.put("Substrates", 1800000.0);
        byCategory.put("Training", 1500000.0);
        byCategory.put("Accessories", 1500000.0);
        var byRegion = new LinkedHashMap<String, Double>();
        byRegion.put("North", 3800000.0);
        byRegion.put("West", 3500000.0);
        byRegion.put("South", 2800000.0);
        byRegion.put("East", 1600000.0);
        byRegion.put("International", 800000.0);
        var byChannel = new LinkedHashMap<String, Double>();
        byChannel.put("Direct Website", 5200000.0);
        byChannel.put("Marketplace", 3800000.0);
        byChannel.put("B2B", 2200000.0);
        byChannel.put("Retail", 1300000.0);
        return new RevenueBreakdown(12500000.0, 11000000.0, 13.6, byCategory, byRegion, byChannel, 850.0, 4500.0);
    }

    public ProfitAnalysis getProfitAnalysis() {
        log.debug("Getting profit analysis");
        return new ProfitAnalysis(5000000.0, 40.0, 3000000.0, 24.0, 6500000.0, 3800000.0, 30.4);
    }

    public double calculateROI(double investment, double return_) {
        if (investment <= 0) return 0.0;
        return Math.round(((return_ - investment) / investment) * 1000.0) / 10.0;
    }

    public Map<String, Object> getFinancialTrends(String period) {
        log.debug("Getting financial trends for: {}", period);
        var trends = new LinkedHashMap<String, Object>();
        trends.put("period", period);
        trends.put("revenueGrowth", 13.6);
        trends.put("profitGrowth", 20.0);
        trends.put("expenseGrowth", 8.3);
        trends.put("marginTrend", "IMPROVING");
        trends.put("projection", "On track for 15% annual growth");
        return trends;
    }
}
