package com.sporekart.ai.usagetracking.application;

import com.sporekart.ai.usagetracking.api.UsageTrackingService;
import com.sporekart.ai.usagetracking.domain.UsageRecord;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordEntity;
import com.sporekart.ai.usagetracking.infrastructure.persistence.UsageRecordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsageTrackingServiceImpl implements UsageTrackingService {

    private final UsageRecordRepository usageRecordRepository;

    public UsageTrackingServiceImpl(UsageRecordRepository usageRecordRepository) {
        this.usageRecordRepository = usageRecordRepository;
    }

    @Override
    public UsageRecord recordUsage(UsageRecord usageRecord) {
        if (usageRecord.getUsageId() == null || usageRecord.getUsageId().isBlank()) {
            usageRecord.setUsageId(UUID.randomUUID().toString());
        }
        if (usageRecord.getTimestamp() == null) {
            usageRecord.setTimestamp(Instant.now());
        }
        if (usageRecord.getTotalTokens() == 0) {
            usageRecord.setTotalTokens(usageRecord.getPromptTokens() + usageRecord.getCompletionTokens());
        }
        UsageRecordEntity saved = usageRecordRepository.save(toEntity(usageRecord));
        return toDomain(saved);
    }

    @Override
    public Optional<UsageRecord> getUsage(String usageId) {
        return usageRecordRepository.findById(usageId).map(this::toDomain);
    }

    @Override
    public List<UsageRecord> listUsage(int limit) {
        int size = limit > 0 ? limit : 100;
        return usageRecordRepository.findAll(PageRequest.of(0, size)).stream()
                .map(this::toDomain)
                .toList();
    }

    private UsageRecordEntity toEntity(UsageRecord record) {
        return new UsageRecordEntity(
                record.getUsageId(),
                record.getRequestId(),
                record.getProviderId(),
                record.getModelId(),
                record.getPromptTokens(),
                record.getCompletionTokens(),
                record.getTotalTokens(),
                record.getExecutionTimeMs(),
                record.isSuccess(),
                record.getFailureReason(),
                record.getTimestamp(),
                record.getUserId(),
                record.getSessionId(),
                record.getModule()
        );
    }

    private UsageRecord toDomain(UsageRecordEntity entity) {
        UsageRecord record = new UsageRecord();
        record.setUsageId(entity.getUsageId());
        record.setRequestId(entity.getRequestId());
        record.setProviderId(entity.getProviderId());
        record.setModelId(entity.getModelId());
        record.setPromptTokens(entity.getPromptTokens());
        record.setCompletionTokens(entity.getCompletionTokens());
        record.setTotalTokens(entity.getTotalTokens());
        record.setExecutionTimeMs(entity.getExecutionTimeMs());
        record.setSuccess(entity.isSuccess());
        record.setFailureReason(entity.getFailureReason());
        record.setTimestamp(entity.getTimestamp());
        record.setUserId(entity.getUserId());
        record.setSessionId(entity.getSessionId());
        record.setModule(entity.getModule());
        return record;
    }
}
