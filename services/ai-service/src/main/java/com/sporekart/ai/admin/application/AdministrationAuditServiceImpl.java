package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.domain.ConfigurationAudit;
import com.sporekart.ai.admin.infrastructure.persistence.AdminAuditEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminAuditRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministrationAuditServiceImpl implements AdministrationAuditService {

    private final AdminAuditRepository adminAuditRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress) {
        var entity = new AdminAuditEntity();
        entity.setAction(action);
        entity.setEntityType(entityType);
        entity.setEntityId(entityId);
        entity.setPerformedBy(performedBy);
        try {
            entity.setDetails(objectMapper.writeValueAsString(details));
        } catch (JsonProcessingException e) {
            entity.setDetails("{}");
        }
        entity.setIpAddress(ipAddress);
        entity.setTimestamp(LocalDateTime.now());
        adminAuditRepository.save(entity);
        log.debug("Audit recorded: {} on {} {}", action, entityType, entityId);
    }

    @Override
    public List<ConfigurationAudit> getAuditLogs(UUID entityId) {
        return adminAuditRepository.findByEntityId(entityId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConfigurationAudit> getAuditLogsByDateRange(Instant from, Instant to) {
        LocalDateTime fromLdt = LocalDateTime.ofInstant(from, ZoneOffset.UTC);
        LocalDateTime toLdt = LocalDateTime.ofInstant(to, ZoneOffset.UTC);
        return adminAuditRepository.findByTimestampBetween(fromLdt, toLdt).stream()
                .map(this::toDomain)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private ConfigurationAudit toDomain(AdminAuditEntity entity) {
        Map<String, Object> details = Map.of();
        if (entity.getDetails() != null) {
            try {
                details = objectMapper.readValue(entity.getDetails(), Map.class);
            } catch (Exception e) {
                // ignore
            }
        }
        return new ConfigurationAudit(
                entity.getId(),
                entity.getAction(),
                entity.getEntityType(),
                entity.getEntityId(),
                entity.getPerformedBy(),
                details,
                entity.getIpAddress(),
                entity.getTimestamp() != null ? entity.getTimestamp().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
