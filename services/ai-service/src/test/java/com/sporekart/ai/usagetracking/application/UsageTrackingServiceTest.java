package com.sporekart.ai.usagetracking.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.usagetracking.domain.DailyUsage;
import com.sporekart.ai.usagetracking.domain.UsageRecord;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordEntity;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UsageTrackingServiceTest {

    @Mock
    private UsageRecordRepository usageRecordRepository;

    @InjectMocks
    private UsageTrackingServiceImpl service;

    @Test
    void recordUsagePersistsRecordAndComputesTotals() {
        UsageRecord record = new UsageRecord();
        record.setProviderId("p1");
        record.setModelId("m1");
        record.setPromptTokens(10);
        record.setCompletionTokens(5);
        record.setTotalTokens(0);
        record.setSuccess(true);
        record.setTimestamp(Instant.now());

        when(usageRecordRepository.save(any(UsageRecordEntity.class))).thenAnswer(i -> i.getArgument(0));

        UsageRecord saved = service.recordUsage(record);

        assertEquals("p1", saved.getProviderId());
        assertEquals(15, saved.getTotalTokens());
    }

    @Test
    void getDailyUsageAggregatesTotals() {
        UsageRecordEntity e1 = new UsageRecordEntity();
        e1.setProviderId("p1");
        e1.setModelId("m1");
        e1.setTotalTokens(10);
        e1.setSuccess(true);
        e1.setExecutionTimeMs(100L);
        e1.setTimestamp(Instant.now());

        UsageRecordEntity e2 = new UsageRecordEntity();
        e2.setProviderId("p1");
        e2.setModelId("m1");
        e2.setTotalTokens(20);
        e2.setSuccess(false);
        e2.setExecutionTimeMs(200L);
        e2.setTimestamp(Instant.now());

        when(usageRecordRepository.findByTimestampBetween(any(), any())).thenReturn(List.of(e1, e2));

        DailyUsage daily = service.getDailyUsage("2024-01-01");

        assertEquals(2, daily.totals().totalRequests());
        assertEquals(30, daily.totals().totalTokens());
        assertEquals(1, daily.totals().totalSuccess());
        assertEquals(1, daily.totals().totalFailure());
    }
}
