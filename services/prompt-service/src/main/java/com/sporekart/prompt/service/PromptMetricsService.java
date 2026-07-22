package com.sporekart.prompt.service;

import com.sporekart.prompt.dto.response.MetricsResponse;
import com.sporekart.prompt.repository.PromptUsageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PromptMetricsService {

    private final PromptUsageRepository usageRepository;

    public PromptMetricsService(PromptUsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    public MetricsResponse getMetrics(UUID templateId) {
        long totalExecutions = usageRepository.countExecutionsByTemplateId(templateId);
        long successful = usageRepository.countSuccessByTemplateId(templateId);
        long failed = usageRepository.countFailureByTemplateId(templateId);
        Double avgLatency = usageRepository.averageLatencyByTemplateId(templateId);
        Double avgTokens = usageRepository.averageTokensByTemplateId(templateId);
        Double avgCost = usageRepository.averageCostByTemplateId(templateId);

        double successRate = totalExecutions > 0 ? (double) successful / totalExecutions * 100 : 0;
        double failureRate = totalExecutions > 0 ? (double) failed / totalExecutions * 100 : 0;

        return new MetricsResponse(
                totalExecutions,
                successful,
                failed,
                avgLatency != null ? avgLatency : 0,
                avgTokens != null ? avgTokens : 0,
                avgCost != null ? BigDecimal.valueOf(avgCost) : BigDecimal.ZERO,
                Math.round(successRate * 100.0) / 100.0,
                Math.round(failureRate * 100.0) / 100.0
        );
    }
}
