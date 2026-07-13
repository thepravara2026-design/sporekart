package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.AdministrationMetricsService;
import com.sporekart.ai.admin.api.AdministrationService;
import com.sporekart.ai.admin.domain.AdminOperation;
import com.sporekart.ai.admin.domain.AdminOperationType;
import com.sporekart.ai.admin.infrastructure.persistence.AdminOperationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminOperationRepository;
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
public class AdministrationServiceImpl implements AdministrationService {

    private final AdminOperationRepository adminOperationRepository;
    private final AdministrationAuditService administrationAuditService;
    private final AdministrationMetricsService administrationMetricsService;
    private final ObjectMapper objectMapper;

    @Override
    public AdminOperation performOperation(AdminOperationType type, String description, Map<String, Object> details, UUID performedBy, String ipAddress) {
        var entity = new AdminOperationEntity();
        entity.setType(type.name());
        entity.setDescription(description);
        try {
            entity.setDetails(objectMapper.writeValueAsString(details));
        } catch (JsonProcessingException e) {
            entity.setDetails("{}");
        }
        entity.setPerformedBy(performedBy);
        entity.setIpAddress(ipAddress);
        entity.setSuccessful(true);
        entity.setPerformedAt(LocalDateTime.now());
        var saved = adminOperationRepository.save(entity);
        administrationAuditService.recordAudit(
                "OPERATION_" + type.name(),
                "AdminOperation",
                saved.getId(),
                performedBy,
                Map.of("description", description, "type", type.name()),
                ipAddress
        );
        administrationMetricsService.getStatistics();
        log.info("Admin operation {} performed by {}: {}", type, performedBy, description);
        return toDomain(saved);
    }

    @Override
    public List<AdminOperation> getOperationHistory() {
        return adminOperationRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AdminOperation> getOperationsByType(AdminOperationType type) {
        return adminOperationRepository.findByType(type.name()).stream()
                .map(this::toDomain)
                .toList();
    }

    private AdminOperation toDomain(AdminOperationEntity entity) {
        Map<String, Object> details = Map.of();
        if (entity.getDetails() != null) {
            try {
                details = objectMapper.readValue(entity.getDetails(), Map.class);
            } catch (Exception e) {
                // ignore
            }
        }
        return new AdminOperation(
                entity.getId(),
                AdminOperationType.valueOf(entity.getType()),
                entity.getDescription(),
                details,
                entity.getPerformedBy(),
                entity.getIpAddress(),
                entity.isSuccessful(),
                entity.getPerformedAt() != null ? entity.getPerformedAt().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
