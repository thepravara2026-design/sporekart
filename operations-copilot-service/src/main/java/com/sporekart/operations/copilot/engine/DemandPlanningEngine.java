package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.*;
import com.sporekart.operations.copilot.dto.ForecastRequest;
import com.sporekart.operations.copilot.dto.ForecastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class DemandPlanningEngine {

    private static final Logger log = LoggerFactory.getLogger(DemandPlanningEngine.class);

    public ForecastResponse forecastDemand(ForecastRequest request) {
        log.info("Forecasting demand for product: {} region: {} days: {}", request.productId(), request.region(), request.forecastDays());
        var days = request.forecastDays() != null ? request.forecastDays() : 30;
        var predicted = calculatePredictedDemand(request.productId(), days);
        var weekly = new LinkedHashMap<String, Integer>();
        for (int w = 0; w < Math.min(4, days / 7); w++) {
            int weekDemand = 0;
            for (int d = w * 7; d < Math.min((w + 1) * 7, days); d++) {
                weekDemand += predicted / days;
            }
            weekly.put("Week " + (w + 1), weekDemand);
        }
        var confidence = 85.0 + (Math.random() * 10);
        var suggestions = List.of(
            "Increase safety stock by 20% for forecasted demand surge",
            "Pre-order raw materials 2 weeks ahead of peak",
            "Consider expanding storage capacity for upcoming season",
            "Review pricing strategy for anticipated demand spike"
        );
        return new ForecastResponse(request.productId(), "Product " + request.productId(), 150,
            predicted, (int) (predicted * 1.2), Math.round(confidence * 10.0) / 10.0, weekly, suggestions);
    }

    public DemandForecast generateDetailedForecast(String sku, String region) {
        log.info("Generating detailed forecast for SKU: {} region: {}", sku, region);
        var dates = new ArrayList<LocalDate>();
        var values = new ArrayList<Integer>();
        var baseDemand = 100 + sku.hashCode() % 200;
        for (int i = 0; i < 30; i++) {
            dates.add(LocalDate.now().plusDays(i));
            var dayFactor = 1.0 + (Math.sin(i * 0.2) * 0.3);
            values.add((int) (baseDemand * dayFactor));
        }
        var regional = new LinkedHashMap<String, Integer>();
        regional.put("north", baseDemand * 30 / 100);
        regional.put("south", baseDemand * 25 / 100);
        regional.put("east", baseDemand * 20 / 100);
        regional.put("west", baseDemand * 25 / 100);
        var festival = new LinkedHashMap<String, Integer>();
        festival.put("diwali", baseDemand * 3);
        festival.put("holi", baseDemand * 2);
        festival.put("new_year", baseDemand * 2);
        var totalDemand = values.stream().mapToInt(Integer::intValue).sum();
        return new DemandForecast(UUID.randomUUID().toString(), sku, "Product " + sku,
            LocalDate.now(), totalDemand, (int) (totalDemand * 0.8), (int) (totalDemand * 1.2),
            88.5, "MODERATE_SEASONALITY", dates, values, regional, festival);
    }

    public double calculateSeasonalFactor(String sku, String season) {
        log.debug("Calculating seasonal factor for SKU: {} season: {}", sku, season);
        return switch (season.toLowerCase()) {
            case "diwali" -> 2.5;
            case "holi" -> 1.8;
            case "pongal" -> 1.5;
            case "onam" -> 1.4;
            case "christmas" -> 2.0;
            case "new_year" -> 1.6;
            default -> 1.0;
        };
    }

    public Map<String, Object> analyzeDemandTrends(String category) {
        log.debug("Analyzing demand trends for category: {}", category);
        var trends = new LinkedHashMap<String, Object>();
        trends.put("category", category);
        trends.put("growthRate", 15.2);
        trends.put("peakMonths", List.of("October", "November", "December"));
        trends.put("lowMonths", List.of("April", "May"));
        trends.put("emergingTrends", List.of("Growing demand for organic spawn", "Increased interest in exotic mushrooms"));
        return trends;
    }

    public int predictInventoryBuffer(int predictedDemand, int leadTimeDays, double serviceLevel) {
        var zScore = serviceLevel >= 95 ? 2.33 : serviceLevel >= 90 ? 1.65 : 1.28;
        var dailyDemand = predictedDemand / 30.0;
        return (int) Math.round(zScore * Math.sqrt(leadTimeDays) * (dailyDemand * 0.3));
    }

    private int calculatePredictedDemand(String productId, int days) {
        var baseRate = Math.abs(productId.hashCode()) % 50 + 20;
        return baseRate * days / 30;
    }
}
