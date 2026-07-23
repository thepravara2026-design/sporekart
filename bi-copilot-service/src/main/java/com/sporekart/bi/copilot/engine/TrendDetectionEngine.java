package com.sporekart.bi.copilot.engine;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.TrendDataPoint;

@Component
public class TrendDetectionEngine {

    private static final Logger log = LoggerFactory.getLogger(TrendDetectionEngine.class);

    public TrendDetectionEngine() {
        log.info("TrendDetectionEngine initialized");
    }

    public List<TrendDataPoint> detectTrends(String metric, List<Double> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) return List.of();

        List<TrendDataPoint> trends = new ArrayList<>();
        List<Double> ma = calculateMovingAverage(dataPoints, 3);
        List<Double> seasonal = calculateSeasonalFactors(dataPoints, 4);

        double[] linearReg = linearRegression(dataPoints);
        double slope = linearReg[0];
        double intercept = linearReg[1];

        for (int i = 0; i < dataPoints.size(); i++) {
            double value = dataPoints.get(i);
            double movAvg = i < ma.size() ? ma.get(i) : value;
            double seasonFactor = i < seasonal.size() ? seasonal.get(i) : 1.0;
            double trendLine = slope * i + intercept;
            double deviation = value - trendLine;
            String direction = value > movAvg ? "up" : value < movAvg ? "down" : "stable";
            double changePct = i > 0 && dataPoints.get(i - 1) != 0
                ? (value - dataPoints.get(i - 1)) / dataPoints.get(i - 1) * 100
                : 0;

            trends.add(new TrendDataPoint(
                UUID.randomUUID().toString(),
                metric,
                "period-" + (i + 1),
                Math.round(value * 100) / 100.0,
                Math.round(movAvg * 100) / 100.0,
                Math.round(seasonFactor * 100) / 100.0,
                Math.round(trendLine * 100) / 100.0,
                Math.round(deviation * 100) / 100.0,
                direction,
                Math.round(changePct * 100) / 100.0,
                OffsetDateTime.now()
            ));
        }
        return trends;
    }

    public List<Double> calculateMovingAverage(List<Double> data, int window) {
        if (data == null || data.isEmpty() || window <= 0) return List.of();

        List<Double> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            int start = Math.max(0, i - window + 1);
            double sum = 0;
            int count = 0;
            for (int j = start; j <= i; j++) {
                sum += data.get(j);
                count++;
            }
            result.add(sum / count);
        }
        return result;
    }

    public List<Double> calculateSeasonalFactors(List<Double> data, int seasonLength) {
        if (data == null || data.isEmpty() || seasonLength <= 0) return List.of();

        List<Double> result = new ArrayList<>();
        double overallAvg = data.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
        if (overallAvg == 0) overallAvg = 1.0;

        for (int i = 0; i < data.size(); i++) {
            double seasonSum = 0;
            int count = 0;
            for (int j = i; j < data.size(); j += seasonLength) {
                seasonSum += data.get(j);
                count++;
            }
            double seasonAvg = count > 0 ? seasonSum / count : overallAvg;
            result.add(seasonAvg / overallAvg);
        }
        return result;
    }

    public List<BusinessInsight> detectGrowthAcceleration(List<TrendDataPoint> trends) {
        if (trends == null || trends.size() < 3) return List.of();

        List<BusinessInsight> insights = new ArrayList<>();

        double recentGrowth = 0;
        int count = 0;
        for (int i = Math.max(0, trends.size() - 3); i < trends.size(); i++) {
            recentGrowth += trends.get(i).changePercent();
            count++;
        }
        recentGrowth = count > 0 ? recentGrowth / count : 0;

        double previousGrowth = 0;
        count = 0;
        for (int i = Math.max(0, trends.size() - 6); i < trends.size() - 3; i++) {
            previousGrowth += trends.get(i).changePercent();
            count++;
        }
        previousGrowth = count > 0 ? previousGrowth / count : 0;

        if (recentGrowth > previousGrowth * 1.5 && recentGrowth > 2.0) {
            String metric = trends.getFirst().metric();
            insights.add(new BusinessInsight(
                UUID.randomUUID().toString(),
                "Growth Acceleration Detected",
                String.format("Growth rate for %s accelerated from %.1f%% to %.1f%%", metric, previousGrowth, recentGrowth),
                "acceleration",
                "positive",
                "Invest additional resources to capitalize on growth momentum",
                0.85,
                Map.of("previousGrowthRate", Math.round(previousGrowth * 100) / 100.0,
                       "recentGrowthRate", Math.round(recentGrowth * 100) / 100.0,
                       "accelerationFactor", Math.round((recentGrowth / Math.max(previousGrowth, 0.1)) * 100) / 100.0),
                List.of(metric),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(30)
            ));
        }

        if (recentGrowth < -5.0) {
            String metric = trends.getFirst().metric();
            insights.add(new BusinessInsight(
                UUID.randomUUID().toString(),
                "Growth Deceleration Warning",
                String.format("Growth rate for %s declined to %.1f%%", metric, recentGrowth),
                "deceleration",
                "warning",
                "Investigate root causes and implement corrective measures",
                0.78,
                Map.of("currentGrowthRate", Math.round(recentGrowth * 100) / 100.0),
                List.of(metric),
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(14)
            ));
        }

        return insights;
    }

    public Map<String, Object> comparePeriods(String currentPeriod, String previousPeriod, String metric) {
        Map<String, Object> comparison = new LinkedHashMap<>();
        comparison.put("metric", metric);
        comparison.put("currentPeriod", currentPeriod);
        comparison.put("previousPeriod", previousPeriod);
        comparison.put("comparisonStatus", "pending_data");

        return comparison;
    }

    public List<TrendDataPoint> getTopTrends(int limit) {
        List<Double> sampleData = new ArrayList<>();
        double value = 100;
        for (int i = 0; i < 12; i++) {
            value += (Math.random() * 10 - 2);
            sampleData.add(value);
        }
        List<TrendDataPoint> trends = detectTrends("sample_metric", sampleData);
        trends.sort((a, b) -> Double.compare(Math.abs(b.deviation()), Math.abs(a.deviation())));
        return trends.subList(0, Math.min(limit, trends.size()));
    }

    private double[] linearRegression(List<Double> data) {
        int n = data.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += data.get(i);
            sumXY += i * data.get(i);
            sumX2 += i * i;
        }

        double denom = n * sumX2 - sumX * sumX;
        double slope = denom != 0 ? (n * sumXY - sumX * sumY) / denom : 0;
        double intercept = denom != 0 ? (sumY - slope * sumX) / n : 0;

        return new double[]{slope, intercept};
    }
}
