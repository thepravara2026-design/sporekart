package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ForecastResult;
import com.sporekart.bi.copilot.domain.ForecastResult.ForecastPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ForecastingEngine {

    private static final Logger log = LoggerFactory.getLogger(ForecastingEngine.class);

    public ForecastingEngine() {
        log.info("Initialized ForecastingEngine");
    }

    public ForecastResult forecast(String metric, String method, int horizon, List<Double> historicalData) {
        log.debug("Forecasting metric={} using method={} horizon={}", metric, method, horizon);
        if (historicalData == null || historicalData.size() < 3) {
            throw new IllegalArgumentException("Historical data must contain at least 3 data points");
        }
        List<ForecastPoint> points = switch (method.toLowerCase()) {
            case "moving_average" -> movingAverageForecast(historicalData, 3, horizon);
            case "exponential_smoothing" -> exponentialSmoothingForecast(historicalData, 0.3, horizon);
            case "linear_regression" -> linearRegressionForecast(historicalData, horizon);
            case "seasonal" -> seasonalForecast(historicalData, 4, horizon);
            default -> throw new IllegalArgumentException("Unknown forecasting method: " + method);
        };
        List<Double> predictedValues = points.stream().map(ForecastPoint::predictedValue).collect(Collectors.toList());
        List<Double> actualValues = historicalData.subList(Math.max(0, historicalData.size() - predictedValues.size()), historicalData.size());
        double accuracy = calculateAccuracy(actualValues, predictedValues);
        String recommendations = generateRecommendations(metric, points, accuracy);
        return new ForecastResult(
                UUID.randomUUID().toString(), metric, method, horizon,
                points, 0.95, accuracy, recommendations
        );
    }

    public List<ForecastPoint> movingAverageForecast(List<Double> data, int window, int horizon) {
        if (window > data.size()) window = data.size();
        List<ForecastPoint> points = new ArrayList<>();
        LocalDate base = LocalDate.now();
        for (int i = 1; i <= horizon; i++) {
            int start = Math.max(0, data.size() - window);
            double avg = data.subList(start, data.size()).stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0);
            double stdDev = stdDev(data.subList(start, data.size()), avg);
            points.add(new ForecastPoint(base.plusMonths(i).toString(), avg, avg - 1.96 * stdDev, avg + 1.96 * stdDev));
            data.add(avg);
        }
        return points;
    }

    public List<ForecastPoint> exponentialSmoothingForecast(List<Double> data, double alpha, int horizon) {
        List<ForecastPoint> points = new ArrayList<>();
        LocalDate base = LocalDate.now();
        double smoothed = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            smoothed = alpha * data.get(i) + (1 - alpha) * smoothed;
        }
        double residualStdDev = 0;
        double prevSmoothed = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            double nextSmoothed = alpha * data.get(i) + (1 - alpha) * prevSmoothed;
            residualStdDev += Math.pow(data.get(i) - nextSmoothed, 2);
            prevSmoothed = nextSmoothed;
        }
        residualStdDev = Math.sqrt(residualStdDev / Math.max(1, data.size() - 1));
        for (int i = 1; i <= horizon; i++) {
            points.add(new ForecastPoint(base.plusMonths(i).toString(), smoothed,
                    smoothed - 1.96 * residualStdDev, smoothed + 1.96 * residualStdDev));
        }
        return points;
    }

    public List<ForecastPoint> linearRegressionForecast(List<Double> data, int horizon) {
        int n = data.size();
        double sumX = IntStream.range(0, n).sum();
        double sumY = data.stream().mapToDouble(Double::doubleValue).sum();
        double sumXY = IntStream.range(0, n).mapToDouble(i -> i * data.get(i)).sum();
        double sumX2 = IntStream.range(0, n).mapToDouble(i -> i * i).sum();
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        double residuals = IntStream.range(0, n).mapToDouble(i -> Math.pow(data.get(i) - (slope * i + intercept), 2)).sum();
        double stdErr = Math.sqrt(residuals / Math.max(1, n - 2));
        LocalDate base = LocalDate.now();
        List<ForecastPoint> points = new ArrayList<>();
        for (int i = 1; i <= horizon; i++) {
            double x = n + i - 1;
            double value = slope * x + intercept;
            points.add(new ForecastPoint(base.plusMonths(i).toString(), value,
                    value - 1.96 * stdErr, value + 1.96 * stdErr));
        }
        return points;
    }

    public List<ForecastPoint> seasonalForecast(List<Double> data, int seasonLength, int horizon) {
        List<ForecastPoint> points = new ArrayList<>();
        LocalDate base = LocalDate.now();
        Map<Integer, List<Double>> seasonalBins = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            seasonalBins.computeIfAbsent(i % seasonLength, k -> new ArrayList<>()).add(data.get(i));
        }
        double[] seasonalFactors = new double[seasonLength];
        double overallAvg = data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        for (int i = 0; i < seasonLength; i++) {
            List<Double> bin = seasonalBins.getOrDefault(i, List.of());
            double binAvg = bin.stream().mapToDouble(Double::doubleValue).average().orElse(overallAvg);
            seasonalFactors[i] = binAvg - overallAvg;
        }
        int n = data.size();
        double sumX = IntStream.range(0, n).sum();
        double sumY = data.stream().mapToDouble(Double::doubleValue).sum();
        double sumXY = IntStream.range(0, n).mapToDouble(i -> i * data.get(i)).sum();
        double sumX2 = IntStream.range(0, n).mapToDouble(i -> i * i).sum();
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        double stdDev = data.stream().mapToDouble(d -> Math.abs(d - overallAvg)).average().orElse(0);
        for (int i = 1; i <= horizon; i++) {
            int idx = n + i - 1;
            double trend = slope * idx + intercept;
            double value = trend + seasonalFactors[(idx) % seasonLength];
            points.add(new ForecastPoint(base.plusMonths(i).toString(), value,
                    value - 1.96 * stdDev, value + 1.96 * stdDev));
        }
        return points;
    }

    public ForecastResult forecastRevenue(int months) {
        List<Double> revenueData = generateSyntheticData(12, 500000, 50000, 0.05);
        return forecast("revenue", "seasonal", months, revenueData);
    }

    public ForecastResult forecastCustomerGrowth(int months) {
        List<Double> customerData = generateSyntheticData(12, 1000, 100, 0.03);
        return forecast("customer_count", "linear_regression", months, customerData);
    }

    public ForecastResult forecastYield(String species, int months) {
        List<Double> yieldData = generateSyntheticData(12, 200, 30, 0.08);
        return forecast(species + "_yield", "exponential_smoothing", months, yieldData);
    }

    public double calculateAccuracy(List<Double> actual, List<Double> predicted) {
        if (actual.isEmpty() || predicted.isEmpty() || actual.size() != predicted.size()) {
            return 0;
        }
        double mape = 0;
        int count = 0;
        for (int i = 0; i < actual.size(); i++) {
            if (actual.get(i) != 0) {
                mape += Math.abs((actual.get(i) - predicted.get(i)) / actual.get(i));
                count++;
            }
        }
        if (count == 0) return 0;
        double accuracy = Math.max(0, 1 - (mape / count));
        return Math.round(accuracy * 10000) / 100.0;
    }

    public String recommendMethod(String metric, List<Double> data) {
        if (data == null || data.size() < 4) return "moving_average";
        boolean hasSeasonality = hasSeasonalPattern(data);
        double trendStrength = calculateTrendStrength(data);
        double volatility = calculateVolatility(data);
        if (hasSeasonality && data.size() >= 8) return "seasonal";
        if (trendStrength > 0.7) return "linear_regression";
        if (volatility < 0.15) return "exponential_smoothing";
        return "moving_average";
    }

    private boolean hasSeasonalPattern(List<Double> data) {
        int n = data.size();
        if (n < 8) return false;
        double autocorrLag4 = autocorrelation(data, 4);
        double autocorrLag1 = autocorrelation(data, 1);
        return Math.abs(autocorrLag4) > Math.abs(autocorrLag1) * 1.5;
    }

    private double autocorrelation(List<Double> data, int lag) {
        double mean = data.stream().mapToDouble(d -> d).average().orElse(0);
        double numerator = 0, denominator = 0;
        for (int i = 0; i < data.size() - lag; i++) {
            numerator += (data.get(i) - mean) * (data.get(i + lag) - mean);
            denominator += Math.pow(data.get(i) - mean, 2);
        }
        return denominator == 0 ? 0 : numerator / denominator;
    }

    private double calculateTrendStrength(List<Double> data) {
        int n = data.size();
        double sumX = IntStream.range(0, n).sum();
        double sumY = data.stream().mapToDouble(d -> d).sum();
        double sumXY = IntStream.range(0, n).mapToDouble(i -> i * data.get(i)).sum();
        double sumX2 = IntStream.range(0, n).mapToDouble(i -> i * i).sum();
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double meanY = sumY / n;
        double ssTotal = data.stream().mapToDouble(d -> Math.pow(d - meanY, 2)).sum();
        double ssResidual = 0;
        for (int i = 0; i < n; i++) {
            double predicted = slope * i + (sumY - slope * sumX) / n;
            ssResidual += Math.pow(data.get(i) - predicted, 2);
        }
        if (ssTotal == 0) return 0;
        double rSquared = 1 - (ssResidual / ssTotal);
        return Math.min(1, Math.max(0, rSquared));
    }

    private double calculateVolatility(List<Double> data) {
        if (data.size() < 2) return 1;
        double mean = data.stream().mapToDouble(d -> d).average().orElse(0);
        double variance = data.stream().mapToDouble(d -> Math.pow(d - mean, 2)).average().orElse(0);
        return Math.sqrt(variance) / (mean == 0 ? 1 : mean);
    }

    private double stdDev(List<Double> values, double mean) {
        if (values.isEmpty()) return 0;
        double variance = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).average().orElse(0);
        return Math.sqrt(variance);
    }

    private List<Double> generateSyntheticData(int points, double base, double amplitude, double noise) {
        Random rng = new Random(42);
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double trend = base + i * (base * 0.02);
            double seasonal = amplitude * Math.sin(2 * Math.PI * i / 12);
            double randomNoise = (rng.nextDouble() - 0.5) * 2 * base * noise;
            data.add(trend + seasonal + randomNoise);
        }
        return data;
    }

    private String generateRecommendations(String metric, List<ForecastPoint> points, double accuracy) {
        if (points.isEmpty()) return "Insufficient data for recommendations.";
        List<String> recs = new ArrayList<>();
        double firstValue = points.get(0).predictedValue();
        double lastValue = points.get(points.size() - 1).predictedValue();
        double changePercent = firstValue == 0 ? 0 : ((lastValue - firstValue) / firstValue) * 100;
        if (changePercent > 10) {
            recs.add("Strong upward trend in " + metric + " - prepare for increased demand");
        } else if (changePercent < -10) {
            recs.add("Downward trend in " + metric + " - investigate causes and consider corrective action");
        }
        if (accuracy < 50) {
            recs.add("Forecast accuracy is low (" + accuracy + "%) - consider using alternative methods or collecting more data");
        } else if (accuracy > 85) {
            recs.add("Forecast accuracy is high (" + accuracy + "%) - model is reliable for planning");
        }
        recs.add("Confidence interval: " + String.format("%.0f%%", points.get(0).confidenceInterval() * 100));
        return String.join("; ", recs);
    }
}
