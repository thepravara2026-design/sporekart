package com.sporekart.admin.infrastructure.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AdminOrderClient {

    private static final Logger log = LoggerFactory.getLogger(AdminOrderClient.class);
    private static final Random RAND = new Random(42);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Map<String, Object>> getOrderAnalytics(String period) {
        log.info("Fetching order analytics for period: {}", period);
        List<Map<String, Object>> analytics = new ArrayList<>();
        int days = switch (period.toLowerCase()) {
            case "today" -> 1;
            case "weekly" -> 7;
            case "monthly" -> 30;
            case "quarterly" -> 90;
            default -> 30;
        };

        LocalDate today = LocalDate.of(2026, 7, 23);
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            int orderCount = 20 + RAND.nextInt(60);
            double revenue = 25000 + RAND.nextDouble() * 75000;

            Map<String, Object> dayData = new LinkedHashMap<>();
            dayData.put("date", date.format(FMT));
            dayData.put("orderCount", orderCount);
            dayData.put("revenue", Math.round(revenue * 100.0) / 100.0);
            dayData.put("averageOrderValue", Math.round((revenue / orderCount) * 100.0) / 100.0);

            Map<String, Integer> statusBreakdown = new LinkedHashMap<>();
            statusBreakdown.put("delivered", (int) (orderCount * 0.75));
            statusBreakdown.put("processing", (int) (orderCount * 0.12));
            statusBreakdown.put("shipped", (int) (orderCount * 0.05));
            statusBreakdown.put("cancelled", (int) (orderCount * 0.05));
            statusBreakdown.put("refunded", (int) (orderCount * 0.03));
            dayData.put("statusBreakdown", statusBreakdown);

            dayData.put("peakOrderHour", 10 + RAND.nextInt(8));
            dayData.put("returnRate", Math.round((2.0 + RAND.nextDouble() * 3.0) * 10.0) / 10.0);

            analytics.add(dayData);
        }
        return analytics;
    }

    public Map<String, Object> getRevenueData(String period) {
        log.info("Fetching revenue data for period: {}", period);
        Map<String, Object> revenueData = new LinkedHashMap<>();
        LocalDate today = LocalDate.of(2026, 7, 23);

        List<Map<String, Object>> dailyRevenue = new ArrayList<>();
        for (int i = 29; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            Map<String, Object> dr = new LinkedHashMap<>();
            dr.put("date", d.format(FMT));
            dr.put("revenue", Math.round((30000 + RAND.nextDouble() * 70000) * 100.0) / 100.0);
            dr.put("orders", 15 + RAND.nextInt(50));
            dailyRevenue.add(dr);
        }

        revenueData.put("dailyRevenue", dailyRevenue);
        revenueData.put("totalRevenue", dailyRevenue.stream()
                .mapToDouble(d -> (double) d.get("revenue")).sum());
        revenueData.put("averageDailyRevenue", dailyRevenue.stream()
                .mapToDouble(d -> (double) d.get("revenue")).average().orElse(0));
        revenueData.put("peakRevenueDay", dailyRevenue.stream()
                .max(Comparator.comparingDouble(d -> (double) d.get("revenue")))
                .map(d -> (String) d.get("date")).orElse("N/A"));

        Map<String, Object> bySource = new LinkedHashMap<>();
        bySource.put("direct", 45.0);
        bySource.put("organic_search", 22.0);
        bySource.put("social_media", 15.0);
        bySource.put("email", 10.0);
        bySource.put("referral", 8.0);
        revenueData.put("revenueBySource", bySource);

        return revenueData;
    }

    public Map<String, Object> getRefundData(String period) {
        log.info("Fetching refund data for period: {}", period);
        Map<String, Object> refundData = new LinkedHashMap<>();
        refundData.put("period", period);
        refundData.put("totalRefunds", 15 + RAND.nextInt(30));
        refundData.put("refundRate", Math.round((1.5 + RAND.nextDouble() * 2.5) * 10.0) / 10.0);
        refundData.put("refundAmount", Math.round((25000 + RAND.nextDouble() * 75000) * 100.0) / 100.0);

        List<Map<String, Object>> reasons = new ArrayList<>();
        String[][] reasonData = {
                {"Product quality issue", "35"},
                {"Late delivery", "22"},
                {"Damaged in transit", "18"},
                {"Wrong item shipped", "12"},
                {"Changed mind", "8"},
                {"Other", "5"}
        };
        for (String[] r : reasonData) {
            Map<String, Object> reason = new LinkedHashMap<>();
            reason.put("reason", r[0]);
            reason.put("percentage", Integer.parseInt(r[1]));
            reasons.add(reason);
        }
        refundData.put("refundReasons", reasons);

        refundData.put("averageProcessingTime", 2 + RAND.nextInt(3));
        refundData.put("pendingRefunds", 3 + RAND.nextInt(8));

        return refundData;
    }
}
