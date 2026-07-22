package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.sporekart.prompt.repository.PromptUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptMetricsServiceTest {

    @Mock
    private PromptUsageRepository usageRepository;

    private PromptMetricsService metricsService;
    private UUID templateId;

    @BeforeEach
    void setUp() {
        metricsService = new PromptMetricsService(usageRepository);
        templateId = UUID.randomUUID();
    }

    @Test
    void shouldGetMetrics() {
        when(usageRepository.countExecutionsByTemplateId(templateId)).thenReturn(100L);
        when(usageRepository.countSuccessByTemplateId(templateId)).thenReturn(90L);
        when(usageRepository.countFailureByTemplateId(templateId)).thenReturn(10L);
        when(usageRepository.averageLatencyByTemplateId(templateId)).thenReturn(250.0);
        when(usageRepository.averageTokensByTemplateId(templateId)).thenReturn(500.0);
        when(usageRepository.averageCostByTemplateId(templateId)).thenReturn(0.002);

        var metrics = metricsService.getMetrics(templateId);
        assertThat(metrics.totalExecutions()).isEqualTo(100);
        assertThat(metrics.successfulExecutions()).isEqualTo(90);
        assertThat(metrics.failedExecutions()).isEqualTo(10);
        assertThat(metrics.successRate()).isEqualTo(90.0);
    }

    @Test
    void shouldHandleZeroExecutions() {
        when(usageRepository.countExecutionsByTemplateId(templateId)).thenReturn(0L);
        when(usageRepository.countSuccessByTemplateId(templateId)).thenReturn(0L);
        when(usageRepository.countFailureByTemplateId(templateId)).thenReturn(0L);
        when(usageRepository.averageLatencyByTemplateId(templateId)).thenReturn(null);
        when(usageRepository.averageTokensByTemplateId(templateId)).thenReturn(null);
        when(usageRepository.averageCostByTemplateId(templateId)).thenReturn(null);

        var metrics = metricsService.getMetrics(templateId);
        assertThat(metrics.totalExecutions()).isZero();
        assertThat(metrics.successRate()).isZero();
    }
}
