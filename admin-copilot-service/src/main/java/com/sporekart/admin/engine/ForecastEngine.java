package com.sporekart.admin.engine;

import com.sporekart.admin.domain.ForecastResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ForecastEngine {

    public ForecastResult forecastRevenue(String period, int horizon) {
        Map<LocalDate, Double> values = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().plusDays(1);
        double baseValue = 45000;
        Random rng = new Random(42);

        for (int i = 0; i < horizon; i++) {
            double seasonalFactor = 1.0 + 0.2 * Math.sin(i * 2 * Math.PI / 30);
            double noise = rng.nextGaussian() * 2000;
            values.put(start.plusDays(i), baseValue * seasonalFactor + noise);
        }

        return new ForecastResult(
            "revenue",
            period,
            values,
            "95%",
            "upward",
            List.of(1.2, 0.9, 1.1, 1.3, 0.8, 1.0, 1.1)
        );
    }

    public ForecastResult forecastSales(String period, int horizon) {
        Map<LocalDate, Double> values = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().plusDays(1);
        double baseValue = 15;
        Random rng = new Random(123);

        for (int i = 0; i < horizon; i++) {
            double seasonalFactor = 1.0 + 0.3 * Math.sin(i * 2 * Math.PI / 7);
            double noise = rng.nextGaussian() * 2;
            values.put(start.plusDays(i), Math.max(0, baseValue * seasonalFactor + noise));
        }

        return new ForecastResult(
            "sales",
            period,
            values,
            "90%",
            "stable",
            List.of(1.3, 0.8, 1.1, 1.2, 0.9, 1.0, 0.7)
        );
    }

    public ForecastResult forecastInventory(String period, int horizon) {
        Map<LocalDate, Double> values = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().plusDays(1);
        double baseValue = 1000;
        Random rng = new Random(456);

        for (int i = 0; i < horizon; i++) {
            double trend = 1.0 + 0.02 * i;
            double noise = rng.nextGaussian() * 50;
            values.put(start.plusDays(i), Math.max(0, baseValue * trend + noise));
        }

        return new ForecastResult(
            "inventory_demand",
            period,
            values,
            "85%",
            "moderate_growth",
            List.of(1.1, 0.95, 1.05, 1.15, 0.9, 1.0, 0.85)
        );
    }
}
