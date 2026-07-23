package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.*;
import com.sporekart.executive.copilot.dto.PerformanceRequest;
import com.sporekart.executive.copilot.dto.PerformanceResponse;
import com.sporekart.executive.copilot.dto.PerformanceResponse.DepartmentSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PerformanceAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(PerformanceAnalyticsEngine.class);

    public PerformanceResponse analyzePerformance(PerformanceRequest request) {
        log.info("Analyzing performance for department: {} period: {}", request.department(), request.period());
        var departments = generateDepartmentSummaries();
        if (request.department() != null && !request.department().isBlank()) {
            departments = departments.stream()
                .filter(d -> d.name().equalsIgnoreCase(request.department()))
                .toList();
        }
        var crossDept = new LinkedHashMap<String, Object>();
        crossDept.put("overallAvgScore", departments.stream().mapToDouble(DepartmentSummary::score).average().orElse(0.0));
        crossDept.put("topDepartment", departments.stream().max(Comparator.comparingDouble(DepartmentSummary::score)).map(DepartmentSummary::name).orElse(""));
        crossDept.put("needsImprovement", departments.stream().min(Comparator.comparingDouble(DepartmentSummary::score)).map(DepartmentSummary::name).orElse(""));
        return new PerformanceResponse(departments, crossDept);
    }

    public DepartmentPerformance getDepartmentDetail(String department) {
        log.debug("Getting department detail: {}", department);
        var metrics = List.of(
            new PerformanceMetric("Revenue", "Financial", 1250000.0, 1100000.0, 1500000.0, "INR", "UP", "GREEN"),
            new PerformanceMetric("Efficiency", "Operational", 87.5, 85.0, 92.0, "%", "UP", "GREEN")
        );
        return new DepartmentPerformance(department, 82.0, 3.5, metrics,
            List.of("Strong team", "Good processes"), List.of("Need more automation"),
            Map.of("vsLastQuarter", 5.2, "vsTarget", -3.0));
    }

    public List<PerformanceMetric> getCrossDepartmentalMetrics() {
        return List.of(
            new PerformanceMetric("Overall Revenue Growth", "Financial", 13.6, 12.0, 20.0, "%", "UP", "GREEN"),
            new PerformanceMetric("Customer Satisfaction", "Customer", 4.5, 4.3, 4.8, "score", "UP", "GREEN"),
            new PerformanceMetric("Employee Productivity", "Operations", 87.5, 85.0, 92.0, "%", "UP", "GREEN"),
            new PerformanceMetric("Marketing ROAS", "Marketing", 5.2, 4.8, 6.0, "x", "UP", "AMBER"),
            new PerformanceMetric("Training NPS", "Training", 72.0, 68.0, 80.0, "score", "UP", "GREEN")
        );
    }

    private List<DepartmentSummary> generateDepartmentSummaries() {
        return List.of(
            new DepartmentSummary("Operations", 85.0, 3.2, List.of("Efficient fulfillment", "High accuracy"), List.of("Warehouse automation")),
            new DepartmentSummary("Marketing", 80.0, 5.0, List.of("Strong ROAS", "Growing reach"), List.of("CAC optimization")),
            new DepartmentSummary("Training", 92.0, 4.5, List.of("High demand", "Excellent feedback"), List.of("Capacity expansion")),
            new DepartmentSummary("Inventory", 72.0, -2.0, List.of("Good turnover", "Quality control"), List.of("Dead stock reduction")),
            new DepartmentSummary("Customer Service", 88.0, 3.0, List.of("Fast response", "High CSAT"), List.of("Self-service portal"))
        );
    }
}
