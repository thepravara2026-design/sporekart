package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskAuditService;
import com.sporekart.ai.risk.domain.RiskAudit;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAuditEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAuditRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskAuditServiceImpl implements RiskAuditService {

    private final RiskAuditRepository riskAuditRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void recordAudit(String action, String entityType, UUID entityId, UUID performedBy,
                             Map<String, Object> details, String ipAddress) {
        var entity = new RiskAuditEntity();
        entity.setId(UUID.randomUUID());
        entity.setAction(action);
        entity.setEntityType(entityType);
        entity.setEntityId(entityId);
        entity.setPerformedBy(performedBy);
        entity.setDetails(toJson(details));
        entity.setIpAddress(ipAddress);
        entity.setTimestamp(LocalDateTime.now());
        riskAuditRepository.save(entity);
        log.info("Audit recorded: {} on {} {} by {}", action, entityType, entityId, performedBy);
    }

    @Override
    public List<RiskAudit> getAuditLogs(UUID entityId) {
        return riskAuditRepository.findByEntityId(entityId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RiskAudit> getAuditLogsByDateRange(Instant from, Instant to) {
        LocalDateTime fromLdt = LocalDateTime.ofInstant(from, ZoneOffset.UTC);
        LocalDateTime toLdt = LocalDateTime.ofInstant(to, ZoneOffset.UTC);
        return riskAuditRepository.findByTimestampBetween(fromLdt, toLdt).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private RiskAudit toDomain(RiskAuditEntity entity) {
        return new RiskAudit(
            entity.getId(), entity.getAction(), entity.getEntityType(),
            entity.getEntityId(), entity.getPerformedBy(),
            fromJson(entity.getDetails(), new TypeReference<Map<String, Object>>() {}),
            entity.getIpAddress(),
            entity.getTimestamp() != null ? entity.getTimestamp().toInstant(ZoneOffset.UTC) : null
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
