package com.sporekart.ai.usagetracking.application;

import com.sporekart.ai.usagetracking.api.UsageDashboardService;
import com.sporekart.ai.usagetracking.domain.UsageSummary;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordEntity;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsageDashboardServiceImpl implements UsageDashboardService {

    private static final int TOP_LIMIT = 10;

    private final UsageRecordRepository usageRecordRepository;

    public UsageDashboardServiceImpl(UsageRecordRepository usageRecordRepository) {
        this.usageRecordRepository = usageRecordRepository;
    }

    @Override
    public Map<String, Object> getDashboard() {
        List<UsageRecordEntity> records = usageRecordRepository.findAll();
        long totalSuccess = usageRecordRepository.countBySuccess(true);
        long totalFailure = usageRecordRepository.countBySuccess(false);
        long totalTokens = 0;
        for (UsageRecordEntity record : records) {
            totalTokens += record.getTotalTokens();
        }
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("totalRequests", (long) records.size());
        dashboard.put("totalSuccess", totalSuccess);
        dashboard.put("totalFailure", totalFailure);
        dashboard.put("totalTokens", totalTokens);
        dashboard.put("topProviders", getTopProviders());
        dashboard.put("topModels", getTopModels());
        dashboard.put("failureRates", getFailureRates());
        return dashboard;
    }

    @Override
    public List<UsageSummary> getTopProviders() {
        return topSummaries(true);
    }

    @Override
    public List<UsageSummary> getTopModels() {
        return topSummaries(false);
    }

    @Override
    public Map<String, Double> getFailureRates() {
        Map<String, int[]> counts = new LinkedHashMap<>();
        for (UsageRecordEntity record : usageRecordRepository.findAll()) {
            int[] value = counts.computeIfAbsent(record.getProviderId(), k -> new int[2]);
            value[0]++;
            if (!record.isSuccess()) {
                value[1]++;
            }
        }
        Map<String, Double> rates = new LinkedHashMap<>();
        for (Map.Entry<String, int[]> entry : counts.entrySet()) {
            int total = entry.getValue()[0];
            int failures = entry.getValue()[1];
            rates.put(entry.getKey(), total > 0 ? (double) failures / total : 0.0);
        }
        return rates;
    }

    private List<UsageSummary> topSummaries(boolean byProvider) {
        Map<String, List<UsageRecordEntity>> grouped = new LinkedHashMap<>();
        for (UsageRecordEntity record : usageRecordRepository.findAll()) {
            String key = byProvider ? record.getProviderId() : record.getModelId();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(record);
        }
        List<UsageSummary> summaries = new ArrayList<>();
        for (List<UsageRecordEntity> group : grouped.values()) {
            summaries.add(buildSummary(group, byProvider));
        }
        summaries.sort(Comparator.comparingInt(UsageSummary::totalRequests).reversed());
        return summaries.size() > TOP_LIMIT ? summaries.subList(0, TOP_LIMIT) : summaries;
    }

    private UsageSummary buildSummary(List<UsageRecordEntity> group, boolean byProvider) {
        int totalRequests = group.size();
        int totalSuccess = 0;
        int totalFailure = 0;
        long totalTokens = 0;
        long totalExecutionTime = 0;
        for (UsageRecordEntity record : group) {
            if (record.isSuccess()) {
                totalSuccess++;
            } else {
                totalFailure++;
            }
            totalTokens += record.getTotalTokens();
            totalExecutionTime += record.getExecutionTimeMs();
        }
        double avg = totalRequests > 0 ? (double) totalExecutionTime / totalRequests : 0.0;
        UsageRecordEntity first = group.get(0);
        String providerId = byProvider ? first.getProviderId() : "ALL";
        String modelId = byProvider ? "ALL" : first.getModelId();
        return new UsageSummary(null, providerId, modelId, totalRequests, totalSuccess, totalFailure, totalTokens, avg);
    }
}
