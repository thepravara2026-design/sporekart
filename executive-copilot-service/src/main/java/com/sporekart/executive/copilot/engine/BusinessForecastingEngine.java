package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.BusinessForecast;
import com.sporekart.executive.copilot.dto.ForecastRequest;
import com.sporekart.executive.copilot.dto.ForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusinessForecastingEngine {

    private static final Logger log = LoggerFactory.getLogger(BusinessForecastingEngine.class);

    public ForecastResponse forecast(ForecastRequest request) {
        log.info("Forecasting type: {} horizon: {} months", request.forecastType(), request.horizonMonths());
        var horizon = request.horizonMonths() != null ? request.horizonMonths() : 12;
        var values = generateProjection(request.forecastType(), horizon);
        var lower = values.stream().map(v -> v * 0.85).toList();
        var upper = values.stream().map(v -> v * 1.15).toList();
        var confidence = 86.5 + (Math.random() * 8);
        var assumptions = new LinkedHashMap<String, Object>();
        assumptions.put("marketGrowthRate", "15% YoY");
        assumptions.put("seasonalityFactor", "1.2x in Q4");
        assumptions.put("marketingEfficiency", "Improving by 5%");
        var risks = List.of("Market competition intensifying", "Supply chain constraints possible", "Regulatory changes in Q3");
        return new ForecastResponse(request.forecastType(), horizon, values, lower, upper,
            Math.round(confidence * 10.0) / 10.0, assumptions, risks);
    }

    public BusinessForecast generateDetailedForecast(String type, int months) {
        log.debug("Generating detailed {} forecast for {} months", type, months);
        var values = generateProjection(type, months);
        var lower = values.stream().map(v -> v * 0.80).toList();
        var upper = values.stream().map(v -> v * 1.20).toList();
        var assumptions = new LinkedHashMap<String, Object>();
        assumptions.put("baseYearGrowth", "15%");
        assumptions.put("inflationRate", "4.5%");
        assumptions.put("capacityExpansion", "Q3 2026");
        return new BusinessForecast(UUID.randomUUID().toString(), type, months, values, lower, upper,
            87.5, assumptions, List.of("Demand variability", "Input cost volatility"));
    }

    public Map<String, Object> forecastRevenue(int months) {
        log.info("Forecasting revenue for {} months", months);
        var values = generateProjection("revenue", months);
        var result = new LinkedHashMap<String, Object>();
        result.put("currentMonthlyRevenue", 1040000.0);
        result.put("projectedMonthlyRevenue", values);
        result.put("totalProjectedRevenue", values.stream().mapToDouble(Double::doubleValue).sum());
        result.put("growthRate", "15.2%");
        result.put("confidence", 87.5);
        result.put("keyDrivers", List.of("New product launches", "Market expansion", "Customer retention improvement"));
        return result;
    }

    public Map<String, Object> forecastOrders(int months) {
        log.debug("Forecasting orders for {} months", months);
        var baseOrders = 2500;
        var values = new ArrayList<Double>();
        for (int i = 0; i < months; i++) {
            var seasonal = 1.0 + (Math.sin(i * 0.5) * 0.2);
            values.add(baseOrders * seasonal * (1 + i * 0.012));
        }
        var result = new LinkedHashMap<String, Object>();
        result.put("currentMonthlyOrders", baseOrders);
        result.put("projectedOrders", values);
        result.put("totalProjectedOrders", values.stream().mapToDouble(Double::doubleValue).sum());
        return result;
    }

    private List<Double> generateProjection(String type, int months) {
        var baseValue = switch (type.toLowerCase()) {
            case "revenue" -> 1040000.0;
            case "orders" -> 2500.0;
            case "customers" -> 500.0;
            case "inventory" -> 450000.0;
            default -> 100000.0;
        };
        var values = new ArrayList<Double>();
        for (int i = 0; i < months; i++) {
            var seasonal = 1.0 + (Math.sin(i * 0.5) * 0.15);
            var growth = 1.0 + (i * 0.012);
            var noise = 0.95 + (Math.random() * 0.1);
            values.add(Math.round(baseValue * seasonal * growth * noise * 100.0) / 100.0);
        }
        return values;
    }
}
