package com.sporekart.bi.copilot.infrastructure.data;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.CultivationAnalytics;
import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.RevenueMetrics;
import com.sporekart.bi.copilot.domain.TrainingAnalytics;

@Component
public class DataAggregationClient {

    private static final Logger log = LoggerFactory.getLogger(DataAggregationClient.class);
    private static final Random RANDOM = new Random(909);

    public DataAggregationClient() {
        log.info("DataAggregationClient initialized");
    }

    public RevenueMetrics getRevenueData(String period) {
        return new RevenueMetrics(
            UUID.randomUUID().toString(),
            period,
            "2025-01-01",
            "2025-01-31",
            250000 + RANDOM.nextDouble() * 100000,
            1200 + RANDOM.nextInt(300),
            200 + RANDOM.nextDouble() * 50,
            180 + RANDOM.nextDouble() * 30,
            Map.of("Oyster", 85000.0, "Shiitake", 65000.0, "Button", 50000.0),
            Map.of("Premium", 120000.0, "Standard", 80000.0, "Economy", 50000.0),
            Map.of("North", 90000.0, "South", 70000.0, "East", 50000.0, "West", 40000.0),
            Map.of("Online", 100000.0, "Retail", 75000.0, "Wholesale", 75000.0),
            8.5,
            230000.0,
            OffsetDateTime.now()
        );
    }

    public CustomerAnalytics getCustomerData(String period) {
        return new CustomerAnalytics(
            UUID.randomUUID().toString(),
            period,
            2800 + RANDOM.nextInt(200),
            80 + RANDOM.nextInt(30),
            25 + RANDOM.nextInt(10),
            3.2 + RANDOM.nextDouble(),
            4800 + RANDOM.nextDouble() * 400,
            750 + RANDOM.nextDouble() * 100,
            87.5 + RANDOM.nextDouble() * 2,
            Map.of("New", 850, "Active", 1200, "Commercial", 80, "Enterprise", 120),
            Map.of("New", 340000.0, "Active", 480000.0, "Commercial", 1800000.0, "Enterprise", 960000.0),
            4.2 + RANDOM.nextDouble() * 0.3,
            2400 + RANDOM.nextInt(100),
            Map.of("North", 800, "South", 600, "East", 500, "West", 400, "Central", 500)
        );
    }

    public TrainingAnalytics getTrainingData(String period) {
        return new TrainingAnalytics(
            UUID.randomUUID().toString(),
            period,
            150 + RANDOM.nextInt(50),
            8 + RANDOM.nextInt(4),
            3 + RANDOM.nextInt(2),
            5 + RANDOM.nextInt(2),
            82 + RANDOM.nextDouble() * 8,
            78 + RANDOM.nextDouble() * 10,
            80 + RANDOM.nextInt(30),
            20 + RANDOM.nextInt(10),
            0.82 + RANDOM.nextDouble() * 0.08,
            Map.of("Sterilization", 85.0, "Substrate", 80.0, "Inoculation", 78.0, "Harvesting", 82.0),
            Map.of("Fundamentals", 60, "Advanced", 40, "Commercial", 30),
            180000 + RANDOM.nextDouble() * 50000,
            95000 + RANDOM.nextDouble() * 20000,
            42.0 + RANDOM.nextDouble() * 8.0
        );
    }

    public CultivationAnalytics getCultivationData(String period) {
        return new CultivationAnalytics(
            UUID.randomUUID().toString(),
            period,
            3500 + RANDOM.nextDouble() * 500,
            320 + RANDOM.nextDouble() * 40,
            Map.of("Oyster", 1200.0, "Shiitake", 900.0, "Button", 800.0),
            Map.of("North", 1000.0, "South", 900.0, "East", 800.0, "West", 800.0),
            38 + RANDOM.nextDouble() * 6,
            0.04 + RANDOM.nextDouble() * 0.03,
            0.03 + RANDOM.nextDouble() * 0.04,
            45 + RANDOM.nextInt(10),
            Map.of("North", 250000.0, "South", 220000.0, "East", 200000.0, "West", 190000.0),
            4.2 + RANDOM.nextDouble() * 0.4
        );
    }

    public Map<String, Object> getPlatformMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("activeUsers", 3200 + RANDOM.nextInt(200));
        metrics.put("totalSessions", 15000 + RANDOM.nextInt(2000));
        metrics.put("avgSessionDurationMs", 480000 + RANDOM.nextInt(60000));
        metrics.put("apiRequestsPerMin", 1200 + RANDOM.nextInt(200));
        metrics.put("errorRate", 0.02 + RANDOM.nextDouble() * 0.01);
        metrics.put("systemUptime", 99.95);
        metrics.put("timestamp", OffsetDateTime.now().toString());
        return metrics;
    }
}
