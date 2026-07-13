package com.sporekart.ai.usagetracking.application;

import com.sporekart.ai.usagetracking.api.UsageAggregationService;
import com.sporekart.ai.usagetracking.domain.DailyUsage;
import com.sporekart.ai.usagetracking.domain.MonthlyUsage;
import com.sporekart.ai.usagetracking.domain.UsageSummary;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordEntity;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsageAggregationServiceImpl implements UsageAggregationService {

    private final UsageRecordRepository usageRecordRepository;

    public UsageAggregationServiceImpl(UsageRecordRepository usageRecordRepository) {
        this.usageRecordRepository = usageRecordRepository;
    }

    @Override
    public DailyUsage getDailyUsage(String date) {
        LocalDate day = LocalDate.parse(date);
        Instant start = day.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = day.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        List<UsageRecordEntity> records = usageRecordRepository.findByTimestampBetween(start, end);
        List<UsageSummary> summaries = summarize(records, date);
        return new DailyUsage(date, summaries, totals(summaries, date));
    }

    @Override
    public MonthlyUsage getMonthlyUsage(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth);
        Instant start = ym.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = ym.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        List<UsageRecordEntity> records = usageRecordRepository.findByTimestampBetween(start, end);
        List<UsageSummary> summaries = summarize(records, yearMonth);
        return new MonthlyUsage(yearMonth, summaries, totals(summaries, yearMonth));
    }

    @Override
    public List<UsageSummary> getByProvider(String providerId) {
        return summarize(usageRecordRepository.findByProviderId(providerId), null);
    }

    @Override
    public List<UsageSummary> getByModel(String modelId) {
        return summarize(usageRecordRepository.findByModelId(modelId), null);
    }

    private List<UsageSummary> summarize(List<UsageRecordEntity> records, String usageDate) {
        Map<String, List<UsageRecordEntity>> grouped = new LinkedHashMap<>();
        for (UsageRecordEntity record : records) {
            String key = record.getProviderId() + "::" + record.getModelId();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(record);
        }
        List<UsageSummary> summaries = new ArrayList<>();
        for (List<UsageRecordEntity> group : grouped.values()) {
            summaries.add(buildSummary(group, usageDate));
        }
        return summaries;
    }

    private UsageSummary buildSummary(List<UsageRecordEntity> group, String usageDate) {
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
        return new UsageSummary(
                usageDate,
                first.getProviderId(),
                first.getModelId(),
                totalRequests,
                totalSuccess,
                totalFailure,
                totalTokens,
                avg
        );
    }

    private UsageSummary totals(List<UsageSummary> summaries, String usageDate) {
        int totalRequests = 0;
        int totalSuccess = 0;
        int totalFailure = 0;
        long totalTokens = 0;
        double weightedExecution = 0;
        for (UsageSummary summary : summaries) {
            totalRequests += summary.totalRequests();
            totalSuccess += summary.totalSuccess();
            totalFailure += summary.totalFailure();
            totalTokens += summary.totalTokens();
            weightedExecution += summary.avgExecutionTimeMs() * summary.totalRequests();
        }
        double avg = totalRequests > 0 ? weightedExecution / totalRequests : 0.0;
        return new UsageSummary(usageDate, "ALL", "ALL", totalRequests, totalSuccess, totalFailure, totalTokens, avg);
    }
}
